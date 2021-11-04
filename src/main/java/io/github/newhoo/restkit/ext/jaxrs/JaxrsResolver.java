package io.github.newhoo.restkit.ext.jaxrs;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.newhoo.restkit.common.RestItem;
import io.github.newhoo.restkit.restful.BaseRequestResolver;
import io.github.newhoo.restkit.restful.MethodPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JaxrsResolver extends BaseRequestResolver {

    @Override
    public String getFrameworkName() {
        return "JAX-RS";
    }

    @Override
    public List<RestItem> getRestItemListInModule(Module module, GlobalSearchScope globalSearchScope) {
        List<RestItem> itemList = new ArrayList<>();

        // 标注了 jaxrs Path 注解的类
        Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(JaxrsPathAnnotation.PATH.getShortName(), module.getProject(), globalSearchScope);

        for (PsiAnnotation psiAnnotation : psiAnnotations) {
            PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
            PsiElement psiElement = psiModifierList.getParent();

            if (!(psiElement instanceof PsiClass)) {
                continue;
            }

            PsiClass psiClass = (PsiClass) psiElement;
            PsiMethod[] psiMethods = psiClass.getMethods();

            String classUriPath = JaxrsAnnotationHelper.getClassUriPath(psiClass);

            for (PsiMethod psiMethod : psiMethods) {
                MethodPath[] methodUriPaths = JaxrsAnnotationHelper.getRequestPaths(psiMethod);

                for (MethodPath methodPath : methodUriPaths) {
                    RestItem item = createRestServiceItem(module, psiMethod, classUriPath, methodPath);
                    itemList.add(item);
                }
            }

        }
        return itemList;
    }
}
