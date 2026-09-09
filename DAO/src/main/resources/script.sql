CREATE TABLE Usuario (
  usuario_id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre_completo      VARCHAR(100) NOT NULL,
  telefono             VARCHAR(20)  NULL,
  correo_electronico   VARCHAR(100) NOT NULL UNIQUE,
  contrasena           VARCHAR(255) NOT NULL,
  esta_activo          BOOLEAN      NOT NULL DEFAULT TRUE,
  descripcion          TEXT         NULL,
  rol                  VARCHAR(20)  NOT NULL,
  nombre_usuario       VARCHAR(50)  NOT NULL UNIQUE,
  fecha_creacion       TIMESTAMP    NOT NULL,
  ultimo_acceso        TIMESTAMP    NULL,
  modificado_por_id    INT          NULL REFERENCES Usuario(usuario_id),
  fecha_modificacion   TIMESTAMP    NULL
);

CREATE TABLE MetodoOperacion (
  metodo_id     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre        VARCHAR(50) NOT NULL,
  cobro_o_pago  VARCHAR(10) NOT NULL
);

CREATE TABLE CategoriaConcepto (
  categoria_id  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  aplica_a      VARCHAR(10)  NOT NULL
);

CREATE TABLE Venta (
  venta_id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id       INT NOT NULL REFERENCES Usuario(usuario_id),
  fecha            DATE NOT NULL,
  descripcion      TEXT NULL,
  estado           VARCHAR(20) NOT NULL,
  concepto_venta   VARCHAR(100) NOT NULL,
  precio_unitario  DECIMAL(10,2) NOT NULL,
  cantidad         INT NOT NULL,
  metodo_id        INT NOT NULL REFERENCES MetodoOperacion(metodo_id),
  subtotal         DECIMAL(10,2) NOT NULL
);

CREATE TABLE Compra (
  compra_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id       INT NOT NULL REFERENCES Usuario(usuario_id),
  fecha            DATE NOT NULL,
  descripcion      TEXT NULL,
  estado           VARCHAR(20) NOT NULL,
  concepto_compra  VARCHAR(100) NOT NULL,
  precio_unitario  DECIMAL(10,2) NOT NULL,
  cantidad         INT NOT NULL,
  metodo_id        INT NOT NULL REFERENCES MetodoOperacion(metodo_id),
  valor_total      DECIMAL(10,2) NOT NULL
);

CREATE TABLE Gasto (
  gasto_id      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id    INT NOT NULL REFERENCES Usuario(usuario_id),
  fecha         DATE NOT NULL,
  descripcion   TEXT NULL,
  estado        VARCHAR(20) NOT NULL,
  categoria_id  INT NOT NULL REFERENCES CategoriaConcepto(categoria_id),
  valor         DECIMAL(10,2) NOT NULL,
  metodo_id     INT NOT NULL REFERENCES MetodoOperacion(metodo_id)
);

CREATE TABLE OtroIngreso (
  otro_ingreso_id  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id       INT NOT NULL REFERENCES Usuario(usuario_id),
  fecha            DATE NOT NULL,
  descripcion      TEXT NULL,
  estado           VARCHAR(20) NOT NULL,
  categoria_id     INT NOT NULL REFERENCES CategoriaConcepto(categoria_id),
  valor            DECIMAL(10,2) NOT NULL,
  metodo_id        INT NOT NULL REFERENCES MetodoOperacion(metodo_id)
);

CREATE TABLE OtroEgreso (
  otro_egreso_id  INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id      INT NOT NULL REFERENCES Usuario(usuario_id),
  fecha           DATE NOT NULL,
  descripcion     TEXT NULL,
  estado          VARCHAR(20) NOT NULL,
  categoria_id    INT NOT NULL REFERENCES CategoriaConcepto(categoria_id),
  valor           DECIMAL(10,2) NOT NULL,
  metodo_id       INT NOT NULL REFERENCES MetodoOperacion(metodo_id)
);

CREATE TABLE ConfiguracionEstadoResultados (
  configuracion_id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id             INT NOT NULL UNIQUE REFERENCES Usuario(usuario_id),
  periodo_inicio_default DATE NOT NULL,
  periodo_fin_default    DATE NOT NULL,
  margen_cmv             DECIMAL(5,2) NOT NULL
);

CREATE TABLE ConfigCategoriaExcluida (
  configuracion_id  INT NOT NULL REFERENCES ConfiguracionEstadoResultados(configuracion_id),
  categoria_id      INT NOT NULL REFERENCES CategoriaConcepto(categoria_id),
  PRIMARY KEY (configuracion_id, categoria_id)
);

CREATE TABLE Vencimiento (
  vencimiento_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  fecha                 DATE NOT NULL,
  ultimos_digitos_cuit  CHAR(4) NULL,
  nombre_cliente        VARCHAR(100) NULL,
  impuesto_pagar        VARCHAR(50) NOT NULL,
  estado                VARCHAR(20) NOT NULL
);
