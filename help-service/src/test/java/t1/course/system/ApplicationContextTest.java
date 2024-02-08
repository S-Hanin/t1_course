package t1.course.system;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import t1.course.MainServlet;
import t1.course.system.core.Wheel;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
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
		assertThat(context.getWheel(TestWheel1.class)).isInstanceOf(TestWheel1.class);
		assertThat(context.getWheel(TestWheel2.class)).isInstanceOf(TestWheel2.class);
		assertThat(context.getWheel(TestWheel2.class).getWheel1()).isInstanceOf(TestWheel1.class);
	}
}



@Wheel
class TestWheel1 {

}

@Wheel
class TestWheel2 {

	@Getter
	private TestWheel1 wheel1;

	public TestWheel2(TestWheel1 wheel) {
		wheel1 = wheel;
	}
}