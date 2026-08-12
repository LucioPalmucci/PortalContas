CREATE DATABASE GestionFinancieraDB;
GO
USE GestionFinancieraDB;
GO

CREATE TABLE Usuario (
  usuario_id         INT IDENTITY(1,1) PRIMARY KEY,
  nombre_completo     VARCHAR(100) NOT NULL,
  telefono            VARCHAR(20)  NULL,
  correo_electronico  VARCHAR(100) NOT NULL UNIQUE,
  contrasena          VARCHAR(255) NOT NULL,
  esta_activo         BIT          NOT NULL DEFAULT 1,
  descripcion         VARCHAR(MAX) NULL,
  rol                 VARCHAR(20)  NOT NULL,
  nombre_usuario      VARCHAR(50)  NOT NULL UNIQUE,
  fecha_creacion      DATETIME     NOT NULL,
  ultimo_acceso       DATETIME     NULL,
  modificado_por_id   INT          NULL FOREIGN KEY REFERENCES Usuario(usuario_id),
  fecha_modificacion  DATETIME     NULL
);
GO

CREATE TABLE MetodoOperacion (
  metodo_id     INT IDENTITY(1,1) PRIMARY KEY,
  nombre        VARCHAR(50) NOT NULL,
  cobro_o_pago  VARCHAR(10) NOT NULL
);
GO

CREATE TABLE CategoriaConcepto (
  categoria_id  INT IDENTITY(1,1) PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  aplica_a      VARCHAR(10)  NOT NULL
);
GO

CREATE TABLE Venta (
  venta_id         INT IDENTITY(1,1) PRIMARY KEY,
  usuario_id       INT NOT NULL FOREIGN KEY REFERENCES Usuario(usuario_id),
  fecha            DATE NOT NULL,
  descripcion      VARCHAR(MAX) NULL,
  estado           VARCHAR(20) NOT NULL,
  concepto_venta   VARCHAR(100) NOT NULL,
  precio_unitario  DECIMAL(10,2) NOT NULL,
  cantidad         INT NOT NULL,
  metodo_id        INT NOT NULL FOREIGN KEY REFERENCES MetodoOperacion(metodo_id),
  subtotal         DECIMAL(10,2) NOT NULL
);
GO

CREATE TABLE Compra (
  compra_id        INT IDENTITY(1,1) PRIMARY KEY,
  usuario_id       INT NOT NULL FOREIGN KEY REFERENCES Usuario(usuario_id),
  fecha            DATE NOT NULL,
  descripcion      VARCHAR(MAX) NULL,
  estado           VARCHAR(20) NOT NULL,
  concepto_compra  VARCHAR(100) NOT NULL,
  precio_unitario  DECIMAL(10,2) NOT NULL,
  cantidad         INT NOT NULL,
  metodo_id        INT NOT NULL FOREIGN KEY REFERENCES MetodoOperacion(metodo_id),
  valor_total      DECIMAL(10,2) NOT NULL
);
GO

CREATE TABLE Gasto (
  gasto_id      INT IDENTITY(1,1) PRIMARY KEY,
  usuario_id    INT NOT NULL FOREIGN KEY REFERENCES Usuario(usuario_id),
  fecha         DATE NOT NULL,
  descripcion   VARCHAR(MAX) NULL,
  estado        VARCHAR(20) NOT NULL,
  categoria_id  INT NOT NULL FOREIGN KEY REFERENCES CategoriaConcepto(categoria_id),
  valor         DECIMAL(10,2) NOT NULL,
  metodo_id     INT NOT NULL FOREIGN KEY REFERENCES MetodoOperacion(metodo_id)
);
GO

CREATE TABLE OtroIngreso (
  otro_ingreso_id  INT IDENTITY(1,1) PRIMARY KEY,
  usuario_id       INT NOT NULL FOREIGN KEY REFERENCES Usuario(usuario_id),
  fecha            DATE NOT NULL,
  descripcion      VARCHAR(MAX) NULL,
  estado           VARCHAR(20) NOT NULL,
  categoria_id     INT NOT NULL FOREIGN KEY REFERENCES CategoriaConcepto(categoria_id),
  valor            DECIMAL(10,2) NOT NULL,
  metodo_id        INT NOT NULL FOREIGN KEY REFERENCES MetodoOperacion(metodo_id)
);
GO

CREATE TABLE OtroEgreso (
  otro_egreso_id  INT IDENTITY(1,1) PRIMARY KEY,
  usuario_id      INT NOT NULL FOREIGN KEY REFERENCES Usuario(usuario_id),
  fecha           DATE NOT NULL,
  descripcion     VARCHAR(MAX) NULL,
  estado          VARCHAR(20) NOT NULL,
  categoria_id    INT NOT NULL FOREIGN KEY REFERENCES CategoriaConcepto(categoria_id),
  valor           DECIMAL(10,2) NOT NULL,
  metodo_id       INT NOT NULL FOREIGN KEY REFERENCES MetodoOperacion(metodo_id)
);
GO

CREATE TABLE ConfiguracionEstadoResultados (
  configuracion_id       INT IDENTITY(1,1) PRIMARY KEY,
  usuario_id             INT NOT NULL UNIQUE FOREIGN KEY REFERENCES Usuario(usuario_id),
  periodo_inicio_default DATE NOT NULL,
  periodo_fin_default    DATE NOT NULL,
  margen_cmv             DECIMAL(5,2) NOT NULL
);
GO

CREATE TABLE ConfigCategoriaExcluida (
  configuracion_id  INT NOT NULL FOREIGN KEY REFERENCES ConfiguracionEstadoResultados(configuracion_id),
  categoria_id      INT NOT NULL FOREIGN KEY REFERENCES CategoriaConcepto(categoria_id),
  PRIMARY KEY (configuracion_id, categoria_id)
);
GO

CREATE TABLE Vencimiento (
  vencimiento_id        INT IDENTITY(1,1) PRIMARY KEY,
  fecha                 DATE NOT NULL,
  ultimos_digitos_cuit  CHAR(4) NULL,
  nombre_cliente        VARCHAR(100) NULL,
  impuesto_pagar        VARCHAR(50) NOT NULL,
  estado                VARCHAR(20) NOT NULL
);
GO

-- Datos de ejemplo
INSERT INTO MetodoOperacion (nombre, cobro_o_pago) VALUES
('Efectivo', 'COBRO'), ('Efectivo', 'PAGO'),
('Transferencia', 'COBRO'), ('Transferencia', 'PAGO'),
('Tarjeta de credito', 'COBRO'), ('Tarjeta de debito', 'PAGO'),
('Cheque', 'COBRO'), ('Cheque', 'PAGO');
GO

INSERT INTO CategoriaConcepto (nombre, aplica_a) VALUES
('Alquiler', 'GASTO'), ('Servicios', 'GASTO'), ('Sueldos', 'GASTO'),
('Marketing', 'GASTO'), ('Mantenimiento', 'GASTO'), ('Otros', 'GASTO'),
('Alquileres cobrados', 'INGRESO'), ('Intereses', 'INGRESO'), ('Subsidios', 'INGRESO'),
('Devoluciones', 'EGRESO'), ('Retiros del titular', 'EGRESO'), ('Deudas no operativas', 'EGRESO');
GO

-- Usuario administrador inicial (usuario: admin / contraseña: Admin1234, hash SHA-256)
INSERT INTO Usuario (nombre_completo, telefono, correo_electronico, contrasena, esta_activo, descripcion, rol, nombre_usuario, fecha_creacion, ultimo_acceso) VALUES
('Administrador del Sistema', NULL, 'admin@lofranosanchez.com', '60fe74406e7f353ed979f350f2fbb6a2e8690a5fa7d1b0c32983d1d8b3f95f67', 1, 'Cuenta de administracion inicial', 'ADMINISTRADOR', 'admin', GETDATE(), NULL);
GO
