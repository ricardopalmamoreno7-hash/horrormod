# Horror Mod (Forge 1.20.1)

Agrega **The Stalker / El Acechador**: un monstruo de 2.8 bloques con brazos larguisimos, cuernos, mandibula abierta y ojos que brillan en la oscuridad.

## Comportamiento
- Te persigue desde hasta 40 bloques.
- **Si lo miras de frente, se queda inmovil** y te observa (a mas de 3 bloques de distancia). Si apartas la vista, corre hacia ti.
- Sus golpes hacen 8 de dano y aplican **Oscuridad** y **Lentitud**.
- Mientras te tiene como objetivo se oye un latido (sonido del Warden).
- Aparece de noche / en zonas oscuras de todo el Overworld (`data/horrormod/forge/biome_modifier/stalker_spawn.json`).
- Huevo generador en la pestana de huevos del modo creativo, o `/summon horrormod:stalker`.

## Como compilarlo
Requisitos: **JDK 17** e internet (Gradle descarga Forge la primera vez).

1. Descarga el MDK oficial de Forge **1.20.1-47.3.0** (files.minecraftforge.net) y copia de ahi `gradlew`, `gradlew.bat` y la carpeta `gradle/` a esta carpeta.
2. Genera el `.jar`:
   ```
   ./gradlew build
   ```
   Queda en `build/libs/horrormod-1.0.0.jar`. Copialo a la carpeta `mods` de tu Minecraft 1.20.1 con Forge 47.x.
3. Para probar en desarrollo: `./gradlew runClient`.

## Estructura
- `entity/StalkerEntity.java`: IA, atributos, sonidos, efectos.
- `client/model/StalkerModel.java`: modelo 3D por codigo y animaciones.
- `client/renderer/`: renderer y capa de ojos brillantes.
- `registry/`: registro de entidad y huevo generador.
- `tools/generate_textures.py`: regenera `stalker.png` y `stalker_eyes.png` (128x128). Puedes editar las texturas en cualquier editor de imagen; respeta el desplegado de cada caja.

## Ajustes rapidos
- Vida, velocidad y dano: `StalkerEntity.createAttributes()`.
- Frecuencia de aparicion: `weight` en `stalker_spawn.json`.
- Tamano del hitbox: `.sized(...)` en `ModEntities.java`.
