package t1.course.system;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import t1.course.MainServlet;
import t1.course.system.core.Wheel;
import t1.course.system.logging.Log;
import t1.course.system.web.UrlHandlerRegistry;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ApplicationContextTest {

	@Test
	void createApplicationContext_shouldNotFail() {
		// when
		var context = new ApplicationContext(new MainServlet());

		// then
		assertThat(context).isNotNull();
	}

	@Test
	void initApplicationContext_shouldInitWheels() {
		// given
		var context = new ApplicationContext(this);

		// when
		context.init();

		// then
		assertThat(context.getWheel(TestWheel2.class)).isInstanceOf(TestWheel2.class);
		assertThat((context.getWheel(TestWheel2.class)).getWheel1()).isInstanceOf(TestWheel.class);
	}

	@Test
	void initApplicationContext_shouldRegisterUrlHandlers() {
		// given
		var context = new ApplicationContext(new MainServlet());

		// when
		context.init();
		var urlRegistry = context.getWheel(UrlHandlerRegistry.class);
		var httpMethodHandlers = urlRegistry.getHttpMethodHandlers();


		// then
		assertThat(httpMethodHandlers.keySet().size()).isPositive();
		assertThat(httpMethodHandlers.values().size()).isPositive();

	}

	@Test
	void initApplicationContext_shouldCreateProxyForObjectsWithLogAnnotatedMethods() {
		// given
		var context = new ApplicationContext(this);

		// when
		context.init();
		var wheel = context.getWheel(TestWheel.class);
		wheel.isLogged();

		// then
		assertThat(wheel.getClass().getSimpleName()).contains("Proxy");

	}


	public interface TestWheel {

		void isLogged();
	}

	@Wheel
	public static class TestWheel1 implements TestWheel{

		@Log
		@Override
		public void isLogged() {
			System.out.println("There should be log message above");
		}

	}

	@Wheel
	public static class TestWheel2 {

		@Getter
		private final TestWheel wheel1;

		public TestWheel2(TestWheel wheel) {
			wheel1 = wheel;
		}
	}
}
