package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import modelo.CPUFoca;
import modelo.Dado;
import modelo.DadoNormal;
import modelo.Evento;
import modelo.GestorTurnos;
import modelo.Juego;
import modelo.Jugador;
import modelo.Partida;
import vista.PantallaJuego;

public class GestorPartida {
    private final Juego juego;
    private final PantallaJuego vista;
    private final Scanner scanner;
    private final GestorJugador gestorJugador;
    private final GestorTablero gestorTablero;
    private final GestorBBDD gestorBBDD;

    public GestorPartida(Juego juego, PantallaJuego vista, Scanner scanner, GestorBBDD gestorBBDD) {
        this.juego = juego;
        this.vista = vista;
        this.scanner = scanner;
        this.gestorJugador = new GestorJugador();
        this.gestorTablero = new GestorTablero();
        this.gestorBBDD = gestorBBDD;
    }

    public void buclePartida() {
        juego.iniciar();

        while (juego.isEnMarcha()) {
            Partida partida = juego.getPartida();
            GestorTurnos turnos = partida.getGestorTurnos();
            Jugador jugadorActual = turnos.getJugadorActual();

            vista.mostrarEstadoGeneral(partida);
            vista.mostrarTurnoJugador(jugadorActual);

            if (jugadorActual.estaBloqueado()) {
                vista.mostrarMensaje(jugadorActual.getNombre() + " pierde el turno.");
                jugadorActual.reducirBloqueo();
                turnos.siguienteTurno();
                continue;
            }

            if (jugadorActual instanceof CPUFoca) {
                ejecutarTurnoCpu((CPUFoca) jugadorActual);
            } else {
                ejecutarTurnoHumano(jugadorActual);
            }

            if (juego.comprobarGanador() != null) {
                vista.mostrarGanador(juego.comprobarGanador());
                juego.setEnMarcha(false);
            } else {
                turnos.siguienteTurno();
            }
        }
    }

    private void ejecutarTurnoHumano(Jugador jugadorActual) {
        boolean finTurno = false;

        while (!finTurno) {
            int opcion = vista.mostrarMenuTurno(scanner, jugadorActual);
            switch (opcion) {
                case 1:
                    lanzarDado(jugadorActual, new DadoNormal());
                    finTurno = true;
                    break;
                case 2:
                    Dado dadoEspecial = vista.seleccionarDadoInventario(scanner, jugadorActual);
                    if (dadoEspecial != null) {
                        lanzarDado(jugadorActual, dadoEspecial);
                        jugadorActual.getInventario().eliminarDado(dadoEspecial);
                        finTurno = true;
                    }
                    break;
                case 3:
                    lanzarBolaDeNieve(jugadorActual);
                    finTurno = true;
                    break;
                case 4:
                    guardarPartida();
                    break;
                case 0:
                    juego.setEnMarcha(false);
                    finTurno = true;
                    break;
                default:
                    vista.mostrarMensaje("Opción incorrecta.");
                    break;
            }
        }
    }

    private void ejecutarTurnoCpu(CPUFoca cpu) {
        String accion = cpu.decidirAccion(juego.getPartida().getJugadores());
        vista.mostrarMensaje("La CPU elige: " + accion);

        if ("bola".equalsIgnoreCase(accion)) {
            lanzarBolaDeNieve(cpu);
        } else {
            Dado dado = cpu.seleccionarDado();
            lanzarDado(cpu, dado);
            if (!(dado instanceof DadoNormal)) {
                cpu.getInventario().eliminarDado(dado);
            }
        }
    }

    private void lanzarDado(Jugador jugador, Dado dado) {
        int pasos = jugador.tirarDado(dado);
        vista.mostrarMensaje(jugador.getNombre() + " ha sacado " + pasos + ".");
        gestorJugador.moverJugador(juego, jugador, pasos);
        vista.mostrarTablero(gestorTablero.generarRepresentacion(juego.getPartida()));

        Evento evento = gestorTablero.aplicarEfectoCasilla(juego, jugador);
        if (evento != null) {
            vista.mostrarMensaje("Evento activado: " + evento.getDescripcion());
        }
    }

    private void lanzarBolaDeNieve(Jugador atacante) {
        List<Jugador> objetivos = new ArrayList<>();
        for (Jugador j : juego.getPartida().getJugadores()) {
            if (!j.equals(atacante)) {
                objetivos.add(j);
            }
        }

        if (objetivos.isEmpty()) {
            vista.mostrarMensaje("No hay objetivos disponibles.");
            return;
        }

        Jugador objetivo = atacante instanceof CPUFoca
                ? objetivos.get(0)
                : vista.seleccionarObjetivo(scanner, objetivos);

        if (objetivo == null) {
            return;
        }

        boolean usado = atacante.usarBolaDeNieve(objetivo);
        if (usado) {
            vista.mostrarMensaje(atacante.getNombre() + " lanza una bola de nieve a " + objetivo.getNombre() + ".");
        } else {
            vista.mostrarMensaje("No quedan bolas de nieve.");
        }
    }

    private void guardarPartida() {
        boolean guardado = gestorBBDD.guardarPartida(juego.getPartida());
        if (guardado) {
            vista.mostrarMensaje("Partida guardada correctamente.");
        } else {
            vista.mostrarMensaje("No se pudo guardar en Oracle. Se ha usado una copia local si estaba disponible.");
        }
    }
}
