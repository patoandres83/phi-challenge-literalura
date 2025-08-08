package literalura.Literalura_desafio;

import literalura.Literalura_desafio.model.Autor;
import literalura.Literalura_desafio.model.Libro;
import literalura.Literalura_desafio.repository.AutorRepository;
import literalura.Literalura_desafio.repository.LibroRepository;
import literalura.Literalura_desafio.service.AutorService;
import literalura.Literalura_desafio.service.GutendexApiService;
import literalura.Literalura_desafio.service.LibroService;
import literalura.Literalura_desafio.service.MenuAplicacion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraDesafioApplication {

	// Necesitamos el contexto de la aplicación para poder cerrarla
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraDesafioApplication.class, args);
		ApplicationContext context = SpringApplication.run(LiteraluraDesafioApplication.class, args);
		int numeroIngresado = -1;
		Scanner scanner = new Scanner(System.in);
		MenuAplicacion menu = new MenuAplicacion();
		GutendexApiService gutendexApiService = new GutendexApiService();
		LibroService libroService = context.getBean(LibroService.class);
		AutorService autorService = context.getBean(AutorService.class);


		while(numeroIngresado!=7){
			menu.mostrarMenuPrincipal();
			try{
				numeroIngresado = scanner.nextInt();
				scanner.nextLine(); // CONSUME EL SALTO DE LÍNEA PENDIENTE DESPUÉS DE nextInt()
				switch (numeroIngresado){
					case 1:
						String nombreDelLibro;
						menu.menuBuscarLibro();
						nombreDelLibro = scanner.nextLine();
						//buscar el libro en la api
						System.out.println("Buscando libro: " + nombreDelLibro + " en la API...");
						//gutendexApiService.consultarApi(nombreDelLibro);
						libroService.consultarYGuardarLibro(nombreDelLibro);
						System.out.println("****** últimos libros buscados*******************************************");
						System.out.println(libroService.getLibrosBuscados());
						System.out.println("*************************************************************************");
						System.out.println(" ");
						System.out.println(" ");
						break;

					case 2:
						System.out.println("*************************************************************************");
						System.out.println("*************************************************************************");
						System.out.println("                                          Listado de libros de Literalura");
						System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
						System.out.println(" ");
						System.out.println(" ");
						List<Libro> librosOrdenados = libroService.listarTodosLosLibrosOrdenadosPorTituloDesc();
						librosOrdenados.forEach(System.out::println);
						break;

					case 3:
						System.out.println("*************************************************************************");
						System.out.println("*************************************************************************");
						System.out.println("                                         Listado de autores de Literalura");
						System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
						System.out.println(" ");
						System.out.println(" ");
						List<Autor> autoresOrdenados = autorService.verTodosLosAutores();
						autoresOrdenados.forEach(System.out::println);
						break;

					case 4:
						int yearBuscado = 0;
						menu.menuBuscarAutorVivoEnAnio();
						yearBuscado = scanner.nextInt();
						List<Autor> autoresVivosEnAnio = autorService.buscarAutoresVivosEn(yearBuscado);
						System.out.println("*************************************************************************");
						System.out.println("*************************************************************************");
						System.out.println("                      Listado de autores vivos en el año:"+yearBuscado+" ");
						System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
						System.out.println(" ");
						System.out.println(" ");
						autoresVivosEnAnio.forEach(System.out::println);
						break;

					case 5:
						libroService.mostrarLibrosEnEspanol();
						break;

					case 6:
						libroService.mostrarLibrosEnInglesStream();
						break;

					case 7:
						System.out.println("Saliendo del sistema...adios!");
						System.out.println(" ");
						System.out.println(" ");
						shutdownApplication(0);
                        break;

					default:
						System.out.println("Opción de menú ingresada no válida, por favor intenta nuevamente.");
						System.out.println(" ");
						System.out.println(" ");
						break;
				}
			}catch (InputMismatchException e){
				System.out.println("¡Error! Debes ingresar un número. Por favor, intenta de nuevo.");
				System.out.println(" ");
				System.out.println(" ");
				scanner.nextLine(); // Limpiar el búfer del scanner de la entrada inválida
				numeroIngresado = -1; // Resetear para que el bucle continúe y pida de nuevo
			}

		}
		scanner.close();



	}

	/*esta función se encarga de apagar la aplicación de spring boot cuando se
	* ingresa la opción 7 del menú principal*/
	public static void shutdownApplication(int exitCode) {
		// Asegura que el contexto no sea nulo antes de intentar cerrarlo
		if (context != null) {
			SpringApplication.exit(context, () -> exitCode);
		} else {
			// Si el contexto es nulo (ej. error durante el inicio), simplemente System.exit
			System.exit(exitCode);
		}
	}

}
