# Tests de Componentes - Frontend Fuentus

Este documento describe la estructura de tests creada para los componentes del frontend de Fuentus.

## 📁 Estructura de Archivos de Test

```
src/__tests__/
├── components/
│   ├── Alert/
│   │   └── Alert.test.tsx           ✅ Completo
│   ├── Badges/
│   │   └── Badge.test.tsx           ✅ Completo  
│   ├── Breadcrumbs/
│   │   └── Breadcrumb.test.tsx      ✅ Completo
│   ├── Buttons/
│   │   └── Button.test.tsx          ✅ Completo
│   ├── Charts/
│   │   └── Charts.test.tsx          📝 Plantilla
│   ├── Forms/
│   │   └── Forms.test.tsx           📝 Plantilla
│   ├── Header/
│   │   └── index.test.tsx           📝 Plantilla
│   ├── Helpers/
│   │   └── Tooltip.test.tsx         ✅ Completo
│   ├── Sidebar/
│   │   └── Sidebar.test.tsx         📝 Plantilla
│   └── Tables/
│       └── TableNucleusServices.test.tsx ✅ Completo
├── CardDataStats.test.tsx           ✅ Completo
├── Footer.test.tsx                  ✅ Completo
├── jest.config.test.ts             ✅ Existente
├── PageTitle.test.tsx              ✅ Existente
└── react-testing-library.test.tsx  ✅ Existente
```

## ✅ Tests Completados

### 1. **CardDataStats.test.tsx**
- ✅ Renderizado con props requeridas
- ✅ Indicadores de nivel (up/down)
- ✅ Clases de modo oscuro
- ✅ Iconos SVG condicionados

### 2. **Footer.test.tsx**
- ✅ Renderizado del componente
- ✅ Logo BBVA
- ✅ Copyright con año actual
- ✅ Clases CSS correctas

### 3. **Alert/Alert.test.tsx**
- ✅ Renderizado con title y message
- ✅ Todas las variantes (success, error, warning, info)
- ✅ Enlaces opcionales
- ✅ Iconos SVG por variante
- ✅ Clases de modo oscuro

### 4. **Badges/Badge.test.tsx**
- ✅ Renderizado básico
- ✅ Tamaños (sm, md)
- ✅ Variantes (light, solid)
- ✅ Colores (primary, success, error, warning, info, light, dark)
- ✅ Iconos de inicio y fin
- ✅ Clases de modo oscuro

### 5. **Breadcrumbs/Breadcrumb.test.tsx**
- ✅ Renderizado con nombre de página
- ✅ Enlace de Dashboard
- ✅ Estructura de navegación
- ✅ Clases CSS correctas
- ✅ Fecha de actualización

### 6. **Buttons/Button.test.tsx**
- ✅ Renderizado básico
- ✅ Tamaños (sm, md, custom)
- ✅ Variantes (primary, outline, success, info, warning, danger)
- ✅ Estados (disabled, enabled)
- ✅ Eventos de click
- ✅ Iconos de inicio y fin
- ✅ Tipos de botón (button, submit, reset)

### 7. **Helpers/Tooltip.test.tsx**
- ✅ Renderizado de children
- ✅ Texto del tooltip
- ✅ Clases CSS correctas
- ✅ Posicionamiento
- ✅ Clases personalizadas

### 8. **Tables/TableNucleusServices.test.tsx**
- ✅ Estado de loading
- ✅ Renderizado con datos
- ✅ Breadcrumb component
- ✅ Información de paginación
- ✅ Datos vacíos y objetos únicos

## 📝 Plantillas Creadas

Los siguientes archivos contienen plantillas comentadas para facilitar la implementación de tests cuando los componentes estén listos:

- **Charts/Charts.test.tsx**: Plantillas para componentes de gráficos
- **Forms/Forms.test.tsx**: Plantillas para MultiSelect y DashboardMultiSelect
- **Header/index.test.tsx**: Plantilla para componente Header
- **Sidebar/Sidebar.test.tsx**: Plantillas para componentes del sidebar

## 🛠️ Configuración Aplicada

### Mocks Configurados
- `window.scrollTo`: Mock para evitar errores de JSDOM
- Warnings de React Router: Suprimidos en entorno de testing
- SVG imports: Mockeados como strings
- Loader component: Mockeado para tests

### TypeScript Config
- ✅ `esModuleInterop: true`
- ✅ `allowSyntheticDefaultImports: true`

### Coverage Mejorado
- **Statements**: 24.43% (mejorado desde 20.9%)
- **Branches**: 15.76% (mejorado desde 2.41%)
- **Functions**: 9.64% (mejorado desde 4.82%)
- **Lines**: 24.55% (mejorado desde 20.68%)

## 📋 Comandos de Testing

```bash
# Ejecutar todos los tests con coverage
npm run test:coverage

# Ejecutar solo tests de componentes
npm test -- --testPathPattern=components

# Ejecutar tests en modo watch
npm run test:watch

# Ejecutar tests específicos
npm test -- Alert.test.tsx
```

## 🎯 Próximos Pasos

1. **Completar tests de Charts**: Implementar tests para ApplicationsCoverageChart, ChartDashboard, CoverageChart
2. **Completar tests de Forms**: Implementar tests para MultiSelect y DashboardMultiSelect
3. **Completar tests de Header**: Implementar tests para componentes del header
4. **Completar tests de Sidebar**: Implementar tests para SidebarLinkGroup y sidebar principal
5. **Mejorar coverage**: Agregar más casos de test para aumentar el coverage

## 📚 Patrones de Testing Utilizados

- **Render with Router**: Wrapper para componentes que usan React Router
- **Mock Functions**: Para callbacks y funciones externas
- **Screen Queries**: Uso de `screen.getByText`, `screen.getByRole`, etc.
- **CSS Class Testing**: Verificación de clases de Tailwind
- **Event Testing**: Testing de clicks y interacciones
- **Prop Testing**: Verificación de diferentes combinaciones de props

## 🔍 Testing Library Queries Utilizadas

- `getByText` / `getAllByText`: Para texto visible
- `getByRole`: Para elementos semánticos
- `getByTestId`: Para elementos con data-testid
- `queryBy*`: Para elementos que pueden no existir
- `toBeInTheDocument()`: Verificar presencia en DOM
- `toHaveClass()`: Verificar clases CSS
- `toHaveAttribute()`: Verificar atributos HTML
