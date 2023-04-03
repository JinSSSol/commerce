package com.zerobase.commerce.order.client;

import com.zerobase.commerce.order.client.mailgun.SendMailForm;
import feign.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

	@PostMapping("sandboxc062a3d34f4a4aaf96a13a31edb17caa.mailgun.org/messages")
	Response sendEmail(@SpringQueryMap SendMailForm form);

}
