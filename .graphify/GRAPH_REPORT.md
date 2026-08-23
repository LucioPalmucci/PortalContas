# Graph Report - .  (2026-08-16)

## Corpus Check
- Corpus is ~27.470 words - fits in a single context window. You may not need a graph.

## Summary
- 1208 nodes · 1722 edges · 77 communities detected
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output
- Edge kinds: method: 919 · calls: 359 · contains: 170 · MODIFIES: 93 · imports: 87 · references: 34 · implements: 20 · inherits: 17 · PARENT_OF: 12 · ON_BRANCH: 11


## Input Scope
- Requested: auto
- Resolved: committed (source: default-auto)
- Included files: 95 · Candidates: 306
- Excluded: 173 untracked · 6 ignored · 2 sensitive · 0 missing committed
- Recommendation: Use --scope all or graphify.yaml inputs.corpus for a knowledge-base folder.

## Graph Freshness
- Built from Git commit: `9766476`
- Compare this hash to `git rev-parse HEAD` before trusting freshness-sensitive graph output.
## God Nodes (most connected - your core abstractions)
1. `cs` - 38 edges
2. `Usuario` - 30 edges
3. `TesoreriaDTO` - 30 edges
4. `xt` - 28 edges
5. `EstadoResultadosDTO` - 26 edges
6. `Compra` - 24 edges
7. `Venta` - 24 edges
8. `qi` - 22 edges
9. `Gasto` - 20 edges
10. `OtroEgreso` - 20 edges

## Surprising Connections (you probably didn't know these)
- `8d9b261 Commit inicial` --ON_BRANCH--> `main`  [EXTRACTED]
  git → git  _Bridges community 24 → community 7_
- `CategoriaConcepto` --implements--> `Serializable`  [EXTRACTED]
  DAO/src/main/java/edu/usal/jdbc/dominio/CategoriaConcepto.java →   _Bridges community 43 → community 24_
- `Compra` --implements--> `Serializable`  [EXTRACTED]
  DAO/src/main/java/edu/usal/jdbc/dominio/Compra.java →   _Bridges community 16 → community 24_
- `ConfiguracionEstadoResultados` --implements--> `Serializable`  [EXTRACTED]
  DAO/src/main/java/edu/usal/jdbc/dominio/ConfiguracionEstadoResultados.java →   _Bridges community 28 → community 24_
- `Gasto` --implements--> `Serializable`  [EXTRACTED]
  DAO/src/main/java/edu/usal/jdbc/dominio/Gasto.java →   _Bridges community 22 → community 24_

## Communities

### Community 24 - "Domain Entities Commit Group"
Cohesion: 0.17
Nodes (2): Serializable, 8d9b261 Commit inicial

### Community 43 - "CategoriaConcepto Domain Entity"
Cohesion: 0.20
Nodes (1): CategoriaConcepto

### Community 16 - "Compra Domain Entity"
Cohesion: 0.09
Nodes (1): Compra

### Community 28 - "ConfiguracionEstadoResultados Domain Entity"
Cohesion: 0.13
Nodes (1): ConfiguracionEstadoResultados

### Community 22 - "Gasto Domain Entity"
Cohesion: 0.11
Nodes (1): Gasto

### Community 44 - "MetodoOperacion Domain Entity"
Cohesion: 0.20
Nodes (1): MetodoOperacion

### Community 20 - "OtroEgreso Domain Entity"
Cohesion: 0.10
Nodes (1): OtroEgreso

### Community 21 - "OtroIngreso Domain Entity"
Cohesion: 0.10
Nodes (1): OtroIngreso

### Community 4 - "Usuario Domain Entity"
Cohesion: 0.07
Nodes (1): Usuario

### Community 29 - "Vencimiento Domain Entity"
Cohesion: 0.13
Nodes (1): Vencimiento

### Community 17 - "Venta Domain Entity"
Cohesion: 0.09
Nodes (1): Venta

### Community 40 - "Period Comparison DTO"
Cohesion: 0.18
Nodes (1): ComparacionPeriodoDTO

### Community 55 - "Category Composition DTO"
Cohesion: 0.22
Nodes (1): ComposicionCategoriaDTO

### Community 23 - "Financial Status DTO"
Cohesion: 0.12
Nodes (1): EstadoFinancieroDTO

### Community 8 - "Income Statement DTO"
Cohesion: 0.07
Nodes (1): EstadoResultadosDTO

### Community 45 - "Trend Point DTO"
Cohesion: 0.20
Nodes (1): PuntoTendenciaDTO

### Community 3 - "Treasury DTO"
Cohesion: 0.06
Nodes (1): TesoreriaDTO

### Community 76 - "Factory Exception Type"
Cohesion: 0.50
Nodes (2): FactoryException, IllegalArgumentException

### Community 15 - "ConfiguracionEstadoResultados Factory and DAO"
Cohesion: 0.11
Nodes (6): HQLException, HibernateException, ConfiguracionEstadoResultadosFactory, ConfiguracionEstadoResultadosDAOImplDb, IConfiguracionEstadoResultadosDAO, ConfiguracionServicio

### Community 77 - "Service Exception Type"
Cohesion: 0.50
Nodes (2): ServiceException, RuntimeException

### Community 18 - "CategoriaConcepto Factory and DAO"
Cohesion: 0.09
Nodes (4): CategoriaConceptoFactory, CategoriaConceptoDAOImplDb, ICategoriaConceptoDAO, CategoriaConceptoServicio

### Community 9 - "Compra Factory and DAO"
Cohesion: 0.08
Nodes (4): CompraFactory, CompraDAOImplDb, ICompraDAO, CompraServicio

### Community 10 - "Gasto Factory and DAO"
Cohesion: 0.08
Nodes (4): GastoFactory, GastoDAOImplDb, IGastoDAO, GastoServicio

### Community 19 - "MetodoOperacion Factory and DAO"
Cohesion: 0.09
Nodes (4): MetodoOperacionFactory, MetodoOperacionDAOImplDb, IMetodoOperacionDAO, MetodoOperacionServicio

### Community 11 - "OtroEgreso Factory and DAO"
Cohesion: 0.08
Nodes (4): OtroEgresoFactory, OtroEgresoDAOImplDb, IOtroEgresoDAO, OtroEgresoServicio

### Community 12 - "OtroIngreso Factory and DAO"
Cohesion: 0.08
Nodes (4): OtroIngresoFactory, OtroIngresoDAOImplDb, IOtroIngresoDAO, OtroIngresoServicio

### Community 5 - "Usuario Factory and DAO"
Cohesion: 0.07
Nodes (4): UsuarioFactory, UsuarioDAOImplDb, IUsuarioDAO, UsuarioServicio

### Community 6 - "Vencimiento Factory and DAO"
Cohesion: 0.07
Nodes (4): VencimientoFactory, VencimientoDAOImplDb, IVencimientoDAO, VencimientoServicio

### Community 13 - "Venta Factory and DAO"
Cohesion: 0.08
Nodes (4): VentaFactory, VentaDAOImplDb, IVentaDAO, VentaServicio

### Community 58 - "CategoriaConcepto DAO Interface"
Cohesion: 0.25
Nodes (1): ICategoriaConceptoDAO

### Community 46 - "Compra DAO Interface"
Cohesion: 0.20
Nodes (1): ICompraDAO

### Community 69 - "ConfiguracionEstadoResultados DAO Interface"
Cohesion: 0.33
Nodes (1): IConfiguracionEstadoResultadosDAO

### Community 47 - "Gasto DAO Interface"
Cohesion: 0.20
Nodes (1): IGastoDAO

### Community 59 - "MetodoOperacion DAO Interface"
Cohesion: 0.25
Nodes (1): IMetodoOperacionDAO

### Community 48 - "OtroEgreso DAO Interface"
Cohesion: 0.20
Nodes (1): IOtroEgresoDAO

### Community 49 - "OtroIngreso DAO Interface"
Cohesion: 0.20
Nodes (1): IOtroIngresoDAO

### Community 36 - "Usuario DAO Interface"
Cohesion: 0.17
Nodes (1): IUsuarioDAO

### Community 50 - "Vencimiento DAO Interface"
Cohesion: 0.20
Nodes (1): IVencimientoDAO

### Community 51 - "Venta DAO Interface"
Cohesion: 0.20
Nodes (1): IVentaDAO

### Community 32 - "Reporte Service Logic"
Cohesion: 0.25
Nodes (1): ReporteServicio

### Community 75 - "Config Properties Utility"
Cohesion: 0.40
Nodes (1): ConfigUtil

### Community 7 - "Git Commit History"
Cohesion: 0.16
Nodes (23): HibernateUtil, Usuario, MetodoOperacion, CategoriaConcepto, Venta, Compra, Gasto, OtroIngreso (+15 more)

### Community 35 - "SQL Schema Tables"
Cohesion: 0.42
Nodes (11): Usuario, MetodoOperacion, CategoriaConcepto, Venta, Compra, Gasto, OtroIngreso, OtroEgreso (+3 more)

### Community 66 - "Expense Composition Bar Chart"
Cohesion: 0.29
Nodes (1): BarraComposicion

### Community 72 - "Catalogo Static Servlet"
Cohesion: 0.53
Nodes (1): CatalogoServlet

### Community 25 - "Support and Session Servlets"
Cohesion: 0.19
Nodes (5): HttpServlet, ParseFechaException, ServiceException, SoporteServlet, TesoreriaServlet

### Community 73 - "Calendar Cell Helper"
Cohesion: 0.33
Nodes (1): CeldaCalendario

### Community 60 - "Compra Management Servlet"
Cohesion: 0.43
Nodes (1): CompraServlet

### Community 54 - "Financial Status Servlet"
Cohesion: 0.40
Nodes (1): EstadoFinancieroServlet

### Community 39 - "Income Statement Servlet"
Cohesion: 0.38
Nodes (1): EstadoResultadosServlet

### Community 74 - "Monthly Evolution Utility"
Cohesion: 0.47
Nodes (1): EvolucionMensualUtil

### Community 61 - "Gasto Management Servlet"
Cohesion: 0.43
Nodes (1): GastoServlet

### Community 67 - "Movement Summary Helper"
Cohesion: 0.29
Nodes (1): MovimientoResumen

### Community 63 - "OtroEgreso Management Servlet"
Cohesion: 0.43
Nodes (1): OtroEgresoServlet

### Community 64 - "OtroIngreso Management Servlet"
Cohesion: 0.43
Nodes (1): OtroIngresoServlet

### Community 68 - "Bar Chart Point Helper"
Cohesion: 0.29
Nodes (1): PuntoBarra

### Community 42 - "Usuario Management Servlet"
Cohesion: 0.38
Nodes (1): UsuarioServlet

### Community 30 - "Vencimiento Calendar Servlet"
Cohesion: 0.27
Nodes (1): VencimientoServlet

### Community 65 - "Venta Management Servlet"
Cohesion: 0.43
Nodes (1): VentaServlet

### Community 62 - "Login and Session Servlet"
Cohesion: 0.46
Nodes (1): loginServlet

### Community 1 - "Bootstrap Bundle Core"
Cohesion: 0.13
Nodes (37): O(), x(), k(), L(), S(), D(), I(), P() (+29 more)

### Community 52 - "Bootstrap Config Utility"
Cohesion: 0.22
Nodes (1): H

### Community 71 - "Bootstrap Base Component Core"
Cohesion: 0.33
Nodes (1): W

### Community 0 - "Bootstrap Dropdown Component"
Cohesion: 0.05
Nodes (4): Q, Y, qi, Ks

### Community 2 - "Bootstrap Swipe Handling"
Cohesion: 0.09
Nodes (2): st, xt

### Community 31 - "Bootstrap Collapse Component"
Cohesion: 0.24
Nodes (1): Bt

### Community 38 - "Bootstrap Base Component"
Cohesion: 0.27
Nodes (1): ui()

### Community 53 - "Bootstrap Focus Trap"
Cohesion: 0.20
Nodes (1): sn

### Community 37 - "Bootstrap Scrollbar Utility"
Cohesion: 0.30
Nodes (1): cn

### Community 34 - "Bootstrap Modal Component"
Cohesion: 0.18
Nodes (1): On

### Community 57 - "Bootstrap Offcanvas Component"
Cohesion: 0.22
Nodes (1): qn

### Community 26 - "Bootstrap Tooltip Template"
Cohesion: 0.19
Nodes (1): Jn

### Community 14 - "Bootstrap Popper Tooltip"
Cohesion: 0.10
Nodes (1): cs

### Community 41 - "Bootstrap Tooltip Content"
Cohesion: 0.24
Nodes (1): us

### Community 27 - "Scrollspy de Bootstrap"
Cohesion: 0.18
Nodes (1): Es

### Community 33 - "Bootstrap Toast Component"
Cohesion: 0.23
Nodes (1): ao

### Community 79 - "Portal Contas README"
Cohesion: 1.00
Nodes (1): Portal Contas

## Knowledge Gaps
- **3 isolated node(s):** `Vencimiento`, `Vencimiento`, `Portal Contas`
  These have ≤1 connection - possible missing edges or undocumented components.
- **Thin community `Domain Entities Commit Group`** (2 nodes): `Serializable`, `8d9b261 Commit inicial`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `CategoriaConcepto Domain Entity`** (1 nodes): `CategoriaConcepto`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Compra Domain Entity`** (1 nodes): `Compra`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `ConfiguracionEstadoResultados Domain Entity`** (1 nodes): `ConfiguracionEstadoResultados`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Gasto Domain Entity`** (1 nodes): `Gasto`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `MetodoOperacion Domain Entity`** (1 nodes): `MetodoOperacion`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `OtroEgreso Domain Entity`** (1 nodes): `OtroEgreso`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `OtroIngreso Domain Entity`** (1 nodes): `OtroIngreso`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Usuario Domain Entity`** (1 nodes): `Usuario`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Vencimiento Domain Entity`** (1 nodes): `Vencimiento`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Venta Domain Entity`** (1 nodes): `Venta`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Period Comparison DTO`** (1 nodes): `ComparacionPeriodoDTO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Category Composition DTO`** (1 nodes): `ComposicionCategoriaDTO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Financial Status DTO`** (1 nodes): `EstadoFinancieroDTO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Income Statement DTO`** (1 nodes): `EstadoResultadosDTO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Trend Point DTO`** (1 nodes): `PuntoTendenciaDTO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Treasury DTO`** (1 nodes): `TesoreriaDTO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Factory Exception Type`** (2 nodes): `FactoryException`, `IllegalArgumentException`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Service Exception Type`** (2 nodes): `ServiceException`, `RuntimeException`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `CategoriaConcepto DAO Interface`** (1 nodes): `ICategoriaConceptoDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Compra DAO Interface`** (1 nodes): `ICompraDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `ConfiguracionEstadoResultados DAO Interface`** (1 nodes): `IConfiguracionEstadoResultadosDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Gasto DAO Interface`** (1 nodes): `IGastoDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `MetodoOperacion DAO Interface`** (1 nodes): `IMetodoOperacionDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `OtroEgreso DAO Interface`** (1 nodes): `IOtroEgresoDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `OtroIngreso DAO Interface`** (1 nodes): `IOtroIngresoDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Usuario DAO Interface`** (1 nodes): `IUsuarioDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Vencimiento DAO Interface`** (1 nodes): `IVencimientoDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Venta DAO Interface`** (1 nodes): `IVentaDAO`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Reporte Service Logic`** (1 nodes): `ReporteServicio`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Config Properties Utility`** (1 nodes): `ConfigUtil`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Expense Composition Bar Chart`** (1 nodes): `BarraComposicion`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Catalogo Static Servlet`** (1 nodes): `CatalogoServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Calendar Cell Helper`** (1 nodes): `CeldaCalendario`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Compra Management Servlet`** (1 nodes): `CompraServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Financial Status Servlet`** (1 nodes): `EstadoFinancieroServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Income Statement Servlet`** (1 nodes): `EstadoResultadosServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Monthly Evolution Utility`** (1 nodes): `EvolucionMensualUtil`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Gasto Management Servlet`** (1 nodes): `GastoServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Movement Summary Helper`** (1 nodes): `MovimientoResumen`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `OtroEgreso Management Servlet`** (1 nodes): `OtroEgresoServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `OtroIngreso Management Servlet`** (1 nodes): `OtroIngresoServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bar Chart Point Helper`** (1 nodes): `PuntoBarra`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Usuario Management Servlet`** (1 nodes): `UsuarioServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Vencimiento Calendar Servlet`** (1 nodes): `VencimientoServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Venta Management Servlet`** (1 nodes): `VentaServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Login and Session Servlet`** (1 nodes): `loginServlet`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Config Utility`** (1 nodes): `H`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Base Component Core`** (1 nodes): `W`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Swipe Handling`** (2 nodes): `st`, `xt`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Collapse Component`** (1 nodes): `Bt`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Base Component`** (1 nodes): `ui()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Focus Trap`** (1 nodes): `sn`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Scrollbar Utility`** (1 nodes): `cn`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Modal Component`** (1 nodes): `On`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Offcanvas Component`** (1 nodes): `qn`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Tooltip Template`** (1 nodes): `Jn`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Popper Tooltip`** (1 nodes): `cs`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Tooltip Content`** (1 nodes): `us`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Scrollspy de Bootstrap`** (1 nodes): `Es`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Bootstrap Toast Component`** (1 nodes): `ao`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Portal Contas README`** (1 nodes): `Portal Contas`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `cs` connect `Bootstrap Popper Tooltip` to `Bootstrap Bundle Core`, `Bootstrap Tooltip Lifecycle`, `Bootstrap Config Utility`, `Bootstrap Tooltip Content`, `Bootstrap Tooltip Template`, `Bootstrap Dropdown Component`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **Why does `Usuario` connect `Usuario Domain Entity` to `Domain Entities Commit Group`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **What connects `Vencimiento`, `Vencimiento`, `Portal Contas` to the rest of the system?**
  _3 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Compra Domain Entity` be split into smaller, more focused modules?**
  _Cohesion score 0.08695652173913043 - nodes in this community are weakly interconnected._
- **Should `ConfiguracionEstadoResultados Domain Entity` be split into smaller, more focused modules?**
  _Cohesion score 0.13333333333333333 - nodes in this community are weakly interconnected._
- **Should `Gasto Domain Entity` be split into smaller, more focused modules?**
  _Cohesion score 0.10526315789473684 - nodes in this community are weakly interconnected._
- **Should `OtroEgreso Domain Entity` be split into smaller, more focused modules?**
  _Cohesion score 0.1 - nodes in this community are weakly interconnected._