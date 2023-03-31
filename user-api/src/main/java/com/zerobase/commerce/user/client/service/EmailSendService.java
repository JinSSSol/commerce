package com.zerobase.commerce.user.client.service;
import com.zerobase.commerce.user.client.MailgunClient;
import com.zerobase.commerce.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailSendService {
	private final MailgunClient mailgunClient;

	public Response sendEmail() {
		SendMailForm form = SendMailForm.builder()
			.from("Excited User <mailgun@sandboxc062a3d34f4a4aaf96a13a31edb17caa.mailgun.org>")
			.to("bbom0802@gmail.com")
			.subject("Test email")
			.text("test")
			.build();
		return mailgunClient.sendEmail(form);
	}
}
