package api.giybat.uz.api.giybat.uz;

import api.giybat.uz.api.giybat.uz.service.SmsSendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private SmsSendService smsSendService;

	@Test
	void contextLoads(){
		smsSendService.getToken();
	}

}
