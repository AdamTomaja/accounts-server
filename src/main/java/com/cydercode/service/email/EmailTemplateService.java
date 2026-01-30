package com.cydercode.service.email;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

  private static final String MAIL_TEMPLATE_PREFIX = "mail/";

  private final SpringTemplateEngine springTemplateEngine;

  public RenderedEmail renderTemplate(String template, Map<String, Object> variables) {
    return new RenderedEmail(
        renderSubject(MAIL_TEMPLATE_PREFIX + template + "/" + template + ".subject", variables),
        renderText(MAIL_TEMPLATE_PREFIX + template + "/" + template + ".txt", variables),
        renderHtml(MAIL_TEMPLATE_PREFIX + template + "/" + template + ".html", variables));
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
