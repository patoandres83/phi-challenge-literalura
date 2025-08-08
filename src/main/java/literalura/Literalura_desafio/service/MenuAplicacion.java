package literalura.Literalura_desafio.service;

import java.util.Scanner;

public class MenuAplicacion {



    public void mostrarMenuPrincipal(){
        System.out.println("---------------------------------------------------");
        System.out.println("----------BIENVENIDO A LITERALURA------------------");
        System.out.println("---------------------------------------------------");
        System.out.println("Menu:                                              ");
        System.out.println("1. Buscar libro por título                         ");
        System.out.println("2. Listar todos los libros de la base de datos     ");
        System.out.println("3. Listar todos los autores de la base de datos    ");
        System.out.println("4. Listar todos los autores vivos en el año        ");
        System.out.println("5. Listar todos los libros en español              ");
        System.out.println("6. Listar todos los libros en inglés              ");
        System.out.println("7. Salir                                           ");
        System.out.println("---------------------------------------------------");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("***************************************************");
        System.out.println("Ingrese una opción del menú para continuar.........");
        System.out.println("***************************************************");
        System.out.println(" ");
        System.out.println(" ");
    }

    public void menuBuscarLibro(){
        System.out.println("***************************************************");
        System.out.println("Ingrese un título de libro para buscar.............");
        System.out.println("***************************************************");
        System.out.println(" ");
        System.out.println(" ");
    }

    public void menuBuscarAutorVivoEnAnio(){
        System.out.println("***************************************************");
        System.out.println("Ingrese un año para buscar autores vivos en ese año");
        System.out.println("***************************************************");
        System.out.println(" ");
        System.out.println(" ");
    }
}
