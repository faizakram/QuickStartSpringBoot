package com.app.controller;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("free-marker")
public class FreeMarkerTemplate {
    private final FreeMarkerConfigurer freemarkerConfigurer;

    FreeMarkerTemplate(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freemarkerConfigurer = freeMarkerConfigurer;
    }

    @PostMapping
    public ResponseEntity<String> baseTemplate(@RequestBody Map<String, Object> templateValue) throws TemplateException, IOException {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("test.ftl");
        return new ResponseEntity<>(FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateValue), HttpStatus.OK);
    }
}
