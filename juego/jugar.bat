@echo off
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.graphics -Djava.library.path=lib -cp Pinguino.jar controlador.Lanzador
pause
