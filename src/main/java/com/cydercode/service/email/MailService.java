package com.cydercode.service.email;

import com.cydercode.config.MailjetConfig;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailjetConfig mailjetConfig;
    private final EmailTemplateService emailTemplateService;

    public void sendTemplate(String username, String email, String templateName, Map<String, Object> variables)
            throws MailjetException {
        RenderedEmail renderedEmail = emailTemplateService.renderTemplate(templateName, variables);
    sendEmail(
        username,
        email,
        renderedEmail.subject(),
        renderedEmail.textContent(),
        renderedEmail.htmlContent());
    }

    public void sendEmail(String username, String email, String subject, String textContent, String htmlContent) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        ClientOptions clientOptions = createClientOptions();
        client = new MailjetClient(clientOptions);
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", mailjetConfig.getFromEmail())
                                        .put("Name", mailjetConfig.getFromName()))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", username)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, textContent)
                                .put(Emailv31.Message.HTMLPART, htmlContent)));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

    private ClientOptions createClientOptions() {
        ClientOptions clientOptions =
            ClientOptions.builder()
                .apiKey(mailjetConfig.getApiKey())
                .apiSecretKey(mailjetConfig.getSecretKey())
                .build();
        return clientOptions;
    }
}
