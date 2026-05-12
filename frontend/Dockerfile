# syntax=docker/dockerfile:1.7

FROM nginx:1.27-alpine

RUN cat <<'EOF' > /usr/share/nginx/html/index.html
<!doctype html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>crud-empleados frontend</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        margin: 0;
        min-height: 100vh;
        display: grid;
        place-items: center;
        background: #f5f7fb;
        color: #1f2937;
      }
      main {
        max-width: 720px;
        padding: 2rem;
        text-align: center;
      }
      h1 {
        margin-bottom: 0.5rem;
      }
    </style>
  </head>
  <body>
    <main>
      <h1>crud-empleados frontend</h1>
      <p>Contenedor base listo para alojar el frontend de la aplicacion.</p>
    </main>
  </body>
</html>
EOF

EXPOSE 80