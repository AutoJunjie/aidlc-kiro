# XHolacracy Frontend

Frontend application for the XHolacracy Management Platform, built with React and TypeScript.

## Technology Stack

- **React 18**
- **TypeScript**
- **React Router** - Routing
- **React Query** - Server state management
- **Ant Design** - UI component library
- **Axios** - HTTP client
- **D3.js / ReactFlow** - Data visualization

## Project Structure

```
src/
├── components/           # Reusable components
│   ├── common/          # Generic UI components
│   ├── visualization/   # Visualization components
│   └── layout/          # Layout components
├── features/            # Feature modules
│   ├── organization/
│   ├── circle/
│   ├── role/
│   ├── proposal/
│   └── meeting/
├── services/            # Service layer
│   ├── api/            # API client
│   └── auth/           # Authentication
├── types/              # TypeScript type definitions
├── utils/              # Utility functions
├── App.tsx             # Application entry
└── index.tsx           # React entry point
```

## Getting Started

### Prerequisites

- Node.js 18+ 
- npm or yarn

### Installation

```bash
npm install
```

### Configuration

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

Edit `.env` with your settings:
```
REACT_APP_API_URL=http://localhost:8080/api/v1
```

### Development

```bash
# Start development server
npm start

# The app will open at http://localhost:3000
```

### Build

```bash
# Create production build
npm run build

# The build folder will contain the optimized production build
```

### Testing

```bash
# Run tests
npm test

# Run tests with coverage
npm test -- --coverage
```

### Linting

```bash
# Run ESLint
npm run lint

# Fix ESLint issues
npm run lint:fix
```

## Features

### Implemented
- Project structure setup
- API client configuration
- Type definitions
- React Query setup
- Ant Design integration

### To Be Implemented
- Organization management
- Circle hierarchy visualization
- Role management
- Proposal workflow
- Governance meeting facilitation

## Development Guidelines

1. **Feature-based structure** - Organize code by feature, not by type
2. **TypeScript** - Use strong typing for all components and functions
3. **React Query** - Use for all server state management
4. **Component composition** - Build complex UIs from simple components
5. **Hooks** - Use custom hooks for reusable logic
6. **Testing** - Write tests for critical functionality

## Available Scripts

- `npm start` - Start development server
- `npm build` - Create production build
- `npm test` - Run tests
- `npm run lint` - Run ESLint
- `npm run lint:fix` - Fix ESLint issues

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

Proprietary - XHolacracy MVP
