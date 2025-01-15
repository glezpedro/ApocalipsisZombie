## Apocalipsis 2024: Supervivencia Zombi en un Tablero

**Apocalipsis 2024** es un juego de tablero cooperativo desarrollado en Java, ambientado en un apocalipsis zombi. Los jugadores controlan a "Supervivientes" que deben colaborar para alcanzar una casilla objetivo mientras enfrentan diversos tipos de zombis en un tablero bidimensional.

---

### Características del Juego

1. **Tablero**: 
   - Dimensiones: 10x10.
   - Inicio: Los Supervivientes comienzan en una esquina, y deben llegar a la casilla opuesta (meta).

2. **Supervivientes**:
   - Pueden realizar hasta 3 acciones por turno: moverse, buscar equipo, atacar zombis, etc.
   - Tienen un inventario limitado y pueden equipar hasta 2 armas.

3. **Zombis**:
   - Varios tipos: Normales, Berserkers, Tóxicos, Corredores y Abominaciones.
   - Cada tipo tiene características únicas como aguante y activaciones por turno.

4. **Equipo**:
   - **Armas**: Cuerpo a cuerpo y a distancia, cada una con potencia, alcance y probabilidad de éxito.
   - **Provisiones**: Necesarias para completar la misión.

5. **Sistema de Combate**:
   - Basado en dados, con éxito definido por el arma seleccionada y las características del zombi.

---

### Funcionalidades Principales

- **Partidas completas**: 
  - Representación del tablero y seguimiento de las acciones.
  - Opciones para cada jugador durante su turno.
- **Simulaciones individuales**:
  - Ataques, activaciones de zombis y búsquedas de equipo.
- **Estadísticas**:
  - Listas de zombis eliminados, heridas de Supervivientes, y registro de ataques.

---

### Requisitos Técnicos

- **Lenguaje**: Java.
- **Interfaz**: Gráfica.
- **Persistencia de Datos**:
  - Guardado y carga de partidas mediante serialización.

---

### Cómo Jugar

1. **Preparar la partida**:
   - Colocar a los Supervivientes en su posición inicial.
   - Generar zombis en posiciones aleatorias.
   
2. **Turnos**:
   - Los Supervivientes realizan sus acciones.
   - Los zombis se activan y atacan.
   - Se añaden nuevos zombis al tablero al final de cada ronda.

3. **Condiciones de victoria**:
   - Todos los Supervivientes alcanzan la meta con provisiones.
   
4. **Condiciones de derrota**:
   - Algún Superviviente es eliminado.

---

### Instalación y Ejecución

1. Ejecutar el archivo `.jar` proporcionado.

---

### Créditos

Desarrollado como proyecto colaborativo por un equipo de programadores entusiastas en Java. Este proyecto fomenta el aprendizaje y la implementación de conceptos avanzados de programación orientada a objetos.
