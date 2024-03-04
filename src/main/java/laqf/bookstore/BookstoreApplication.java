package laqf.bookstore;

import laqf.bookstore.presentation.BookForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext contextSpring =
				new SpringApplicationBuilder(BookstoreApplication.class)
						.headless(false)
						.web(WebApplicationType.NONE)
						.run(args);
		// Load Form

		EventQueue.invokeLater(()->{

			BookForm bookForm = contextSpring.getBean(BookForm.class);
			bookForm.setVisible(true);

		});
	}

}
