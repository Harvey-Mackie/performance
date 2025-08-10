import React, { useState } from 'react';
import { Container, Row, Col, Card, Button, Nav, Alert, Badge, Tabs, Tab, ButtonGroup } from 'react-bootstrap';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

// Mock data with more diverse test types
const mockDomains = [
  {
    id: 'cash',
    name: 'CASH',
    description: 'Cash payment processing',
    tests: [
      { type: 'volume', name: 'Basic Volume Test', lastRun: '2024-02-09', status: 'success' },
      { type: 'stress', name: 'Peak Load Test - 20k Records', lastRun: '2024-02-08', status: 'failed' },
      { type: 'endurance', name: '24h Endurance Test', lastRun: '2024-02-07', status: 'success' },
      { type: 'custom', name: 'Bulk Processing - FDBA SFTP', lastRun: '2024-02-03', status: 'success' },
      { type: 'custom', name: 'Error Scenarios', lastRun: '2024-02-02', status: 'failed' }
    ]
  },
  {
    id: 'bacs',
    name: 'BACS',
    description: 'BACS payment processing',
    tests: [
      { type: 'volume', name: 'Standard Volume', lastRun: '2024-02-09', status: 'success' },
    ]
  }
];

const mockMetricsData = [
  { time: '0s', tps: 950, latency: 120, errors: 0 },
  { time: '10s', tps: 980, latency: 135, errors: 2 },
  { time: '20s', tps: 1020, latency: 145, errors: 5 },
  { time: '30s', tps: 990, latency: 130, errors: 1 }
];

const App = () => {
  const [selectedDomain, setSelectedDomain] = useState(mockDomains[0]);
  const [activeTest, setActiveTest] = useState(null);
  const [selectedTestType, setSelectedTestType] = useState('all');

  // Group tests by type
  const getTestsByType = (tests) => {
    const grouped = tests.reduce((acc, test) => {
      if (!acc[test.type]) {
        acc[test.type] = [];
      }
      acc[test.type].push(test);
      return acc;
    }, {});
    return grouped;
  };

  // Filter tests based on selected type
  const getFilteredTests = (tests) => {
    if (selectedTestType === 'all') return tests;
    return tests.filter(test => test.type === selectedTestType);
  };

  const getTestIcon = (type) => {
    switch (type) {
      case 'volume': return 'speedometer2';
      case 'stress': return 'lightning-charge';
      case 'endurance': return 'clock-history';
      case 'custom': return 'gear';
      default: return 'file-earmark-text';
    }
  };

  return (
    <Container fluid className="p-4 bg-light min-vh-100">
      {/* Header */}
      <Row className="mb-4">
        <Col>
          <h2 className="fw-bold mb-1">Testing Dashboard 🎯</h2>
          <p className="text-muted">Monitor and execute performance tests across domains</p>
        </Col>
        <Col xs="auto">
          <Button variant="light" className="rounded-circle p-2">
            <i className="bi bi-arrow-clockwise"></i>
          </Button>
        </Col>
      </Row>

      {/* Tabs */}
      <Tabs
        activeKey={selectedDomain.id}
        onSelect={(key) => {
          setSelectedDomain(mockDomains.find(d => d.id === key));
          setActiveTest(null);
        }}
        className="mb-4"
      >
        {mockDomains.map(domain => (
          <Tab key={domain.id} eventKey={domain.id} title={domain.name}>
            <Card className="border-0 shadow-sm">
              <Card.Header className="bg-white border-0 py-3">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <h5 className="mb-1">{domain.name}</h5>
                    <p className="text-muted mb-0 small">{domain.description}</p>
                  </div>
                  <ButtonGroup size="sm">
                    <Button 
                      variant={selectedTestType === 'all' ? 'primary' : 'outline-primary'}
                      onClick={() => setSelectedTestType('all')}
                      className="fw-medium"
                    >
                      All Tests
                    </Button>
                    {Object.keys(getTestsByType(domain.tests)).map(type => (
                      <Button
                        key={type}
                        variant={selectedTestType === type ? 'primary' : 'outline-primary'}
                        onClick={() => setSelectedTestType(type)}
                        className="fw-medium"
                      >
                        <i className={`bi bi-${
                          type === 'volume' ? 'speedometer2' :
                          type === 'stress' ? 'lightning-charge' :
                          type === 'endurance' ? 'clock-history' :
                          'gear'
                        } me-1`}></i>
                        {type.charAt(0).toUpperCase() + type.slice(1)}
                      </Button>
                    ))}
                  </ButtonGroup>
                </div>
              </Card.Header>
              <Card.Body className="p-3">
                <div className="d-flex flex-column gap-3">
                  {getFilteredTests(domain.tests).map((test, idx) => (
                    <div 
                      key={idx} 
                      className="test-container p-4 border rounded bg-white"
                    >
                      <div className="mb-2">
                        <div>
                          <h5>{test.name}</h5>
                          <small>Last run: {test.lastRun}</small>
                        </div>
                      </div>
                      <div className="d-flex justify-content-between align-items-center">
                        <Badge 
                          bg={
                            test.status === 'success' ? 'success' :
                            test.status === 'failed' ? 'danger' : 'warning'
                          }
                          className="p-2"
                        >
                          <i className={`bi bi-${
                            test.status === 'success' ? 'check-circle' :
                            test.status === 'failed' ? 'x-circle' : 'exclamation-circle'
                          } me-1`}></i>
                          {test.status}
                        </Badge>
                        <Button 
                          variant="primary"
                          size="md"
                          className="px-3"
                          onClick={() => setActiveTest(test)}
                        >
                          <i className="bi bi-play-fill me-1"></i>
                          View Test Details
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              </Card.Body>
            </Card>
          </Tab>
        ))}
      </Tabs>

      {/* Active Test Panel and Alerts stay the same */}

      <style>
        {`
          .test-container {
            border-color: #dee2e6 !important;
            transition: all 0.2s ease-in-out;
          }
          
          .test-container:hover {
            border-color: #adb5bd !important;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
          }

          .nav-tabs .nav-link {
            border: none;
            padding: 0.75rem 1rem;
            font-weight: 500;
          }
          
          .nav-tabs .nav-link.active {
            border-bottom: 2px solid var(--bs-primary);
          }
        `}
      </style>
      </Container>
  );
};

export default App;
