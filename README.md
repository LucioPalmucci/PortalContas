# Portal Contas
 
Sistema de gestión financiera para los clientes de contas.ar.
 
## Instalación
 
Este proyecto se instala de forma manual con IntelliJ. No usa `npm`.
 
### Requisitos previos
 
- **JDK 21**
- **Apache Tomcat 9.0.109**
- **IntelliJ IDEA**
### Pasos
 
1. Cloná el repositorio:
```bash
   git clone https://github.com/LucioPalmucci/PortalContas.git
   cd PortalContas
```
 
2. Abrí el proyecto en **IntelliJ IDEA**.
3. Configurá el JDK del proyecto en `File > Project Structure > Project SDK` y seleccioná **JDK 21**.
4. Configurá Tomcat como servidor de aplicaciones: `Run > Edit Configurations > Add New Configuration > Tomcat Server > Local`, y apuntá a tu instalación de **Tomcat 9.0.109**.
5. Desplegá el artefacto (`.war`) en la pestaña **Deployment** de la configuración de Tomcat.
6. Ejecutá la configuración desde IntelliJ para levantar el proyecto en Tomcat.

```
 
## Cómo colaborar
 
Las contribuciones se realizan mediante **pull requests**:
 
1. Hacé un fork del repositorio.
2. Creá una rama para tu cambio (`git checkout -b mi-mejora`).
3. Hacé commit de tus cambios con un mensaje descriptivo.
4. Enviá un pull request describiendo qué resuelve o mejora tu cambio.
## Licencia
 
Este proyecto está licenciado bajo la **Licencia MIT**. Todos los derechos reservados.
