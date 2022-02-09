package io.github.newhoo.restkit.ext.jaxrs;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceExpression;
import io.github.newhoo.restkit.feature.javaimpl.MethodPath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JaxrsAnnotationHelper {

    private static String getWsPathValue(PsiAnnotation annotation) {
        String value = getAnnotationAttributeValue(annotation, "value");

        return value != null ? value : "";
    }

    private static String getAnnotationAttributeValue(PsiAnnotation annotation, String attr) {
        List<String> values = getAnnotationAttributeValues(annotation, attr);
        if (!values.isEmpty()) {
            return values.get(0);
        }
        return null;
    }

    @NotNull
    private static List<String> getAnnotationAttributeValues(PsiAnnotation annotation, String attr) {
        PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attr);

        List<String> values = new ArrayList<>();
        //只有注解
        //一个值 class com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
        //多个值  class com.intellij.psi.impl.source.tree.java.PsiArrayInitializerMemberValueImpl
        if (value instanceof PsiReferenceExpression) {
            PsiReferenceExpression expression = (PsiReferenceExpression) value;
            values.add(expression.getText());
        } else if (value instanceof PsiLiteralExpression) {
//            values.add(psiNameValuePair.getLiteralValue());
            values.add(((PsiLiteralExpression) value).getValue().toString());
        } else if (value instanceof PsiArrayInitializerMemberValue) {
            PsiAnnotationMemberValue[] initializers = ((PsiArrayInitializerMemberValue) value).getInitializers();

            for (PsiAnnotationMemberValue initializer : initializers) {
                values.add(initializer.getText().replaceAll("\\\"", ""));
            }
        }

        return values;
    }

    /**
     * 过滤所有注解
     *
     * @param psiMethod
     */
    public static MethodPath[] getRequestPaths(PsiMethod psiMethod) {
        PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
        List<MethodPath> list = new ArrayList<>();

        PsiAnnotation wsPathAnnotation = psiMethod.getModifierList().findAnnotation(JaxrsPathAnnotation.PATH.getQualifiedName());
        String path = wsPathAnnotation == null ? psiMethod.getName() : getWsPathValue(wsPathAnnotation);

        JaxrsHttpMethodAnnotation[] jaxrsHttpMethodAnnotations = JaxrsHttpMethodAnnotation.values();

        /*for (PsiAnnotation annotation : annotations) {
            for (JaxrsHttpMethodAnnotation methodAnnotation : jaxrsHttpMethodAnnotations) {
                if (annotation.getQualifiedName().equals(methodAnnotation.getQualifiedName())) {
                    list.add(new RequestPath(path, methodAnnotation.getShortName()));
                }
            }
        }*/

        Arrays.stream(annotations).forEach(a -> Arrays.stream(jaxrsHttpMethodAnnotations).forEach(methodAnnotation -> {
            if (a.getQualifiedName().equals(methodAnnotation.getQualifiedName())) {
                list.add(new MethodPath(path, methodAnnotation.getShortName()));
            }
        }));

        return list.toArray(new MethodPath[0]);
    }

    public static String getClassUriPath(PsiClass psiClass) {
        PsiAnnotation annotation = psiClass.getModifierList().findAnnotation(JaxrsPathAnnotation.PATH.getQualifiedName());

        String path = getAnnotationAttributeValue(annotation, "value");

        return path != null ? path : "";
    }

//    public static String getMethodUriPath(PsiMethod psiMethod) {
//        JaxrsHttpMethodAnnotation requestAnnotation = null;
//
//        List<JaxrsHttpMethodAnnotation> httpMethodAnnotations = Arrays.stream(JaxrsHttpMethodAnnotation.values()).filter(annotation ->
//                psiMethod.getModifierList().findAnnotation(annotation.getQualifiedName()) != null
//        ).collect(Collectors.toList());
//
//       /* if (httpMethodAnnotations.size() == 0) {
//            requestAnnotation = null;
//        }*/
//
//        if (httpMethodAnnotations.size() > 0) {
//            requestAnnotation = httpMethodAnnotations.get(0);
//        }
//
//        String mappingPath;
//        if (requestAnnotation != null) {
//            PsiAnnotation annotation = psiMethod.getModifierList().findAnnotation(JaxrsPathAnnotation.PATH.getQualifiedName());
//            mappingPath = getWsPathValue(annotation);
//        } else {
//            String methodName = psiMethod.getName();
//            mappingPath = StringUtils.uncapitalize(methodName);
//        }
//
//        return mappingPath;
//    }

}