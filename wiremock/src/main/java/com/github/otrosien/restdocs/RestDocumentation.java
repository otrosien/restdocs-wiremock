package com.github.otrosien.restdocs;

import org.springframework.restdocs.JUnitRestDocumentation;

public final class RestDocumentation {

    // utility class.
    private RestDocumentation() {}

    public static JUnitRestDocumentation usingGradleDir() {
        return new JUnitRestDocumentation("build/generated-snippets");
    }

}
