#!/usr/bin/env python3
"""Genera stalker.png y stalker_eyes.png (128x128) sin dependencias externas.

Uso (desde la carpeta del proyecto):
    python3 tools/generate_textures.py

Cada caja del modelo usa el desplegado estandar de Minecraft:
    texOffs(u, v) con caja (w, h, d) ocupa 2*(d+w) x (d+h) pixeles.
Si cambias el modelo, ajusta BOXES para que coincida.
"""
import os
import random
import struct
import sys
import zlib

W = H = 128
random.seed(1337)

OUT = sys.argv[1] if len(sys.argv) > 1 else "src/main/resources/assets/horrormod/textures/entity"

BONE = (205, 200, 180)
BLOOD = (95, 10, 12)


def new_canvas():
    return [[(0, 0, 0, 0) for _ in range(W)] for _ in range(H)]


def write_png(path, px):
    raw = b"".join(b"\x00" + bytes(c for p in row for c in p) for row in px)

    def chunk(tag, data):
        body = tag + data
        return struct.pack(">I", len(data)) + body + struct.pack(">I", zlib.crc32(body) & 0xFFFFFFFF)

    png = (b"\x89PNG\r\n\x1a\n"
           + chunk(b"IHDR", struct.pack(">IIBBBBB", W, H, 8, 6, 0, 0, 0))
           + chunk(b"IDAT", zlib.compress(raw, 9))
           + chunk(b"IEND", b""))
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(png)


def faces(u, v, w, h, d):
    return {
        "top": (u + d, v, w, d),
        "bottom": (u + d + w, v, w, d),
        "right": (u, v + d, d, h),
        "front": (u + d, v + d, w, h),
        "left": (u + d + w, v + d, d, h),
        "back": (u + 2 * d + w, v + d, w, h),
    }


def clamp(x):
    return max(0, min(255, int(x)))


def shade(c, amount):
    return (clamp(c[0] + amount), clamp(c[1] + amount), clamp(c[2] + amount), 255)


def put(img, x, y, color):
    if 0 <= x < W and 0 <= y < H:
        img[y][x] = color


def rect(img, x, y, w, h, color):
    for j in range(y, y + h):
        for i in range(x, x + w):
            put(img, i, j, color)


def fill_noise(img, r, base, noise=8):
    x, y, w, h = r
    for j in range(y, y + h):
        for i in range(x, x + w):
            img[j][i] = shade(base, random.randint(-noise, noise))


def lrect(img, face, lx, ly, w, h, color):
    rect(img, face[0] + lx, face[1] + ly, w, h, color)


def splatter(img, r, count):
    x, y, w, h = r
    for _ in range(count):
        cx = random.randint(x, x + w - 1)
        cy = random.randint(y, y + h - 1)
        for _ in range(random.randint(2, 6)):
            put(img, cx + random.randint(-1, 1), cy + random.randint(-1, 1),
                (random.randint(60, 115), random.randint(5, 14), random.randint(6, 14), 255))


def crack(img, r, length=5):
    x, y, w, h = r
    cx = random.randint(x, x + w - 1)
    cy = random.randint(y, y + h - 1)
    for _ in range(length):
        put(img, cx, cy, (12, 10, 12, 255))
        cx += random.choice((-1, 0, 1))
        cy += random.choice((0, 1))


# nombre: (u, v, w, h, d, color base)
BOXES = {
    "head":     (0, 0, 6, 8, 6, (122, 117, 110)),
    "jaw":      (30, 0, 5, 5, 6, (72, 62, 62)),
    "spine":    (60, 0, 2, 14, 2, (70, 64, 58)),
    "rhorn":    (70, 0, 1, 5, 1, (200, 190, 170)),
    "lhorn":    (76, 0, 1, 5, 1, (200, 190, 170)),
    "claws":    (84, 0, 1, 5, 1, BONE),
    "body":     (0, 16, 8, 18, 4, (28, 26, 30)),
    "rarm":     (0, 40, 3, 26, 3, (34, 32, 36)),
    "larm":     (14, 40, 3, 26, 3, (34, 32, 36)),
    "rhand":    (28, 40, 4, 5, 4, (20, 18, 20)),
    "lhand":    (46, 40, 4, 5, 4, (20, 18, 20)),
    "rleg":     (0, 72, 3, 18, 3, (30, 28, 32)),
    "lleg":     (14, 72, 3, 18, 3, (30, 28, 32)),
}


def build_skin():
    img = new_canvas()
    F = {name: faces(*spec[:5]) for name, spec in BOXES.items()}

    # Base con ruido en cada cara
    for name, spec in BOXES.items():
        for r in F[name].values():
            fill_noise(img, r, spec[5])

    # --- Cabeza: cuenca oscura, nariz, grietas
    hf = F["head"]["front"]
    lrect(img, hf, 0, 2, 6, 3, (8, 6, 8, 255))      # banda de las cuencas
    lrect(img, hf, 2, 5, 2, 2, (22, 14, 14, 255))   # fosas nasales
    lrect(img, hf, 0, 7, 6, 1, (34, 10, 10, 255))   # linea de la boca
    for face in F["head"].values():
        for _ in range(2):
            crack(img, face)
    for k in ("top",):
        fill_noise(img, F["head"][k], (60, 55, 50))

    # --- Mandibula: dientes arriba, interior rojo
    jf = F["jaw"]["front"]
    for lx in range(5):
        lrect(img, jf, lx, 0, 1, 1, (232, 226, 208, 255) if lx % 2 == 0 else (60, 10, 10, 255))
    for lx in (0, 4):
        lrect(img, jf, lx, 1, 1, 1, (232, 226, 208, 255))
    fill_noise(img, F["jaw"]["top"], (96, 12, 14))
    jt = F["jaw"]["top"]
    for ly in range(0, 6, 2):
        lrect(img, jt, 0, ly, 1, 1, (232, 226, 208, 255))
        lrect(img, jt, 4, ly, 1, 1, (232, 226, 208, 255))
    for side in ("right", "left"):
        s = F["jaw"][side]
        for lx in range(0, 6, 2):
            lrect(img, s, lx, 0, 1, 1, (232, 226, 208, 255))

    # --- Torso: costillas y vertebras
    bf = F["body"]["front"]
    for ly in (3, 6, 9, 12):
        lrect(img, bf, 1, ly, 6, 1, (96, 91, 84, 255))
    lrect(img, bf, 3, 1, 2, 14, (80, 76, 72, 255))
    bb = F["body"]["back"]
    for ly in range(0, 17, 2):
        lrect(img, bb, 3, ly, 2, 1, (92, 88, 82, 255))

    # --- Articulaciones (codos y rodillas)
    for arm in ("rarm", "larm"):
        for face in ("right", "front", "left", "back"):
            lrect(img, F[arm][face], 0, 12, F[arm][face][2], 2, (72, 68, 64, 255))
    for leg in ("rleg", "lleg"):
        for face in ("right", "front", "left", "back"):
            lrect(img, F[leg][face], 0, 8, F[leg][face][2], 2, (66, 62, 58, 255))

    # --- Manos: bordes claros tipo hueso
    for hand in ("rhand", "lhand"):
        for face in ("right", "front", "left", "back"):
            lrect(img, F[hand][face], 0, 3, F[hand][face][2], 2, (150, 145, 130, 255))

    # --- Sangre seca
    for key in ("body", "rarm", "larm", "rhand", "lhand", "head"):
        for face in F[key].values():
            splatter(img, face, 3)
    return img


def build_eyes():
    img = new_canvas()
    hf = faces(*BOXES["head"][:5])["front"]
    red = (255, 20, 20, 255)
    core = (255, 205, 110, 255)
    # dos ojos de 2x2 en la banda de las cuencas
    for ex in (0, 4):
        lrect(img, hf, ex, 3, 2, 2, red)
    put(img, hf[0] + 1, hf[1] + 4, core)
    put(img, hf[0] + 4, hf[1] + 4, core)
    # interior de la boca con brillo tenue
    jt = faces(*BOXES["jaw"][:5])["top"]
    lrect(img, jt, 1, 1, 3, 4, (150, 8, 8, 255))
    return img


if __name__ == "__main__":
    write_png(os.path.join(OUT, "stalker.png"), build_skin())
    write_png(os.path.join(OUT, "stalker_eyes.png"), build_eyes())
    print("Texturas generadas en", OUT)
