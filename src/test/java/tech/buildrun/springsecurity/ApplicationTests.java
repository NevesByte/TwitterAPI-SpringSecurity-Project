package tech.buildrun.springsecurity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requer banco de dados externo para subir o contexto completo")
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
