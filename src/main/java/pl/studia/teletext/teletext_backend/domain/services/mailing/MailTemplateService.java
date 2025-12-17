package pl.studia.teletext.teletext_backend.domain.services.mailing;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class MailTemplateService {
  private final SpringTemplateEngine templateEngine;

  public String render(String templateName, Map<String, Object> variables) {
    var context = new Context();
    context.setVariables(variables);
    return templateEngine.process(templateName, context);
  }
}
