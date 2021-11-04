package io.github.newhoo.restkit.ext.jaxrs;

public enum JaxrsHttpMethodAnnotation {
    GET("javax.ws.rs.GET", "GET"),
    POST("javax.ws.rs.POST", "POST"),
    PUT("javax.ws.rs.PUT", "PUT"),
    DELETE("javax.ws.rs.DELETE", "DELETE"),
    HEAD("javax.ws.rs.HEAD", "HEAD"),
    PATCH("javax.ws.rs.PATCH", "PATCH");

    JaxrsHttpMethodAnnotation(String qualifiedName, String methodName) {
        this.qualifiedName = qualifiedName;
        this.methodName = methodName;
    }

    private String qualifiedName;
    private String methodName;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getShortName() {
        return qualifiedName.substring(qualifiedName.lastIndexOf(".") - 1);
    }
}