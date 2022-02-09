package io.github.newhoo.restkit.ext.jaxrs;

import com.intellij.openapi.project.Project;
import io.github.newhoo.restkit.restful.RequestResolver;
import io.github.newhoo.restkit.restful.ep.RestfulResolverProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author huzunrong
 * @since 1.0.0
 */
public class JaxrsResolverProvider implements RestfulResolverProvider {

    @Override
    public RequestResolver createRequestResolver(@NotNull Project project) {
        return new JaxrsResolver();
    }
}
