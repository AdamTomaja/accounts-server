package com.cydercode.service.email;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final SpringTemplateEngine springTemplateEngine;

    public RenderedEmail renderTemplate(String template, Map<String, Object> variables) {
        return new RenderedEmail(
                renderSubject(template + ".subject", variables),
                renderText(template, variables),
                renderHtml(template , variables));
    }

    public String renderHtml(String template, Map<String, Object> variables) {
        return render(template, variables);
    }

    public String renderText(String template, Map<String, Object> variables) {
       return render(template, variables);
    }

    public String renderSubject(String template, Map<String, Object> variables) {
        return render(template, variables);
    }

    private String render(String template, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return springTemplateEngine.process(template, context);
    }

}
