package controlador;

import java.util.Scanner;

import modelo.Juego;
import modelo.Partida;
import vista.PantallaJuego;
import vista.PantallaMenu;

public class MainController {
    private final PantallaMenu pantallaMenu;
    private final Scanner scanner;
    private final GestorBBDD gestorBBDD;

    public MainController() {
        this.pantallaMenu = new PantallaMenu();
        this.scanner = new Scanner(System.in);
        this.gestorBBDD = new GestorBBDD();
    }

    public void ejecutar() {
        boolean salir = false;

        while (!salir) {
            int opcion = pantallaMenu.mostrarMenuPrincipal(scanner);
            switch (opcion) {
                case 1:
                    iniciarNuevaPartida();
                    break;
                case 2:
                    cargarPartida();
                    break;
                case 3:
                    pantallaMenu.mostrarAyuda();
                    break;
                case 0:
                    salir = true;
                    pantallaMenu.mostrarMensaje("Hasta pronto.");
                    break;
                default:
                    pantallaMenu.mostrarMensaje("Opción no válida.");
                    break;
            }
        }
        scanner.close();
    }

    private void iniciarNuevaPartida() {
        String nombreJugador = pantallaMenu.pedirNombreJugador(scanner);
        int casillas = pantallaMenu.pedirNumeroCasillas(scanner);

        Juego juego = Juego.crearPartidaNueva(nombreJugador, casillas);
        PantallaJuego vista = new PantallaJuego();
        GestorPartida gestorPartida = new GestorPartida(juego, vista, scanner, gestorBBDD);
        gestorPartida.buclePartida();
    }

    private void cargarPartida() {
        int idPartida = pantallaMenu.pedirIdPartida(scanner);
        Partida partida = gestorBBDD.cargarPartida(idPartida);

        if (partida == null) {
            pantallaMenu.mostrarMensaje("No se ha encontrado la partida.");
            return;
        }

        Juego juego = new Juego(partida);
        PantallaJuego vista = new PantallaJuego();
        GestorPartida gestorPartida = new GestorPartida(juego, vista, scanner, gestorBBDD);
        pantallaMenu.mostrarMensaje("Partida cargada correctamente.");
        gestorPartida.buclePartida();
    }
}
