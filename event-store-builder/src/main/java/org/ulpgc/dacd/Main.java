package org.ulpgc.dacd;

public class Main {
    public static void main(String[] args) throws StoreException {
        WeatherEventReceiver weatherEventReceiver = new WeatherEventReceiver(args[0]);
        weatherEventReceiver.messageReceiver();

        PriceEventReceiver priceEventReceiver = new PriceEventReceiver(args[1]);
        priceEventReceiver.messageReceiver();
    }
}

/*
Ambos en una clase => unirlo

business Unit => se tiene que suscribir al broker(real time) tmb y del data lake lo coge, no tiene poequé, pero nos da datos anteriores. Pero data lake builder hay que guardarlo siempre, a mi no em hace falta, pero si arrancamos la unidad de negocio y los que dan datos no estan encendidos, no emiten datos por lo que no tendrá info ej tiempo real, por ello tal vez necesitas los ultimos datos del data lake => el ultimo fichero de cada tpico y cogo los ultimos datos
o simplemente hacer suscrito durable!! y asi no tenemos que entrar al data lake => si nos interesa el ultimo dato

Datamart => base de datos que vamos a usar pa sacar info pa pagina web o apiRest => alguna manera pa mostrar los datos
si hago pagina web => añadir capturas en el readme

la buisness unit está suscrita de manera durablea los topicos que necesita y a medida que nos van llegando los eventos, los va mezclando => dos listas, mapa, dos tablas en una base de datos...; no tiene porque usarse base datos

 pa pag web usaria javaScript con llamadas al servidor

 ocpiones pal datamart => estructura que nos permita almacenar de manera facil y rapida => el datamart es un modelo que tenga 2 clases que guarde los datos => donde guardamos los datos mezclados, preparados para mostrar; info que no use de eventos => quitar, solo dejar lo mas importante, lo que queramos pa responder al usuario => respuestas. Ej: donde me voy este fin de semana => le doy info del lugar con mejor tiempo y el nombre del mejor hotel por ejemplo, con un enlace al tripadvisor e info del hotel. dejo una estructura preparada pa q se vaya actualizando con el mismo hotel; luego lo mostramos en una pag web, en linea de comandos, api rest... La mas facil => api rest, monto servicio y monto endpoint y pa lante => respuestas a pregunta del cliente => puedo por linea de comanods => toda esa info la muestra el business unit
 guardo info preparada en memoria/actualizo => y de ahi coger e imprimiendo la info como respuesta => eliminom y actualizo la info que me parezca mejor.

 Actualizar info hoteles => 1 hotel por isla
 Luego ver que compensa mas: temperatura, viento, precipitaciones, nubosidad y precio! dar pa usuario las dos opciones => mejor tiempo y mejor precio, mejor precio-temperatura
 TODO SE CALCULA EN EL BUSINESS UNIT y lo calculado lo guardas tal cual al datamart => no tiene porque parecerse a los eventos
 en consola que lo solicite, o que lo actualice solo => no hace falta que lo solicite, lo arranco, tengo datos e imprimo info
 lo importante es la interoperabilidad entre los módulos
 */
