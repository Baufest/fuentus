/**
 * Test básico para validar la configuración de Jest
 */
describe('Jest Configuration Tests', () => {
  test('Jest está funcionando correctamente', () => {
    expect(true).toBe(true);
  });

  test('Jest puede manejar matemáticas básicas', () => {
    expect(2 + 2).toBe(4);
    expect(5 * 3).toBe(15);
  });

  test('Jest puede manejar strings', () => {
    const greeting = 'Hello World';
    expect(greeting).toBe('Hello World');
    expect(greeting).toHaveLength(11);
  });

  test('Jest puede manejar arrays', () => {
    const fruits = ['apple', 'banana', 'orange'];
    expect(fruits).toHaveLength(3);
    expect(fruits).toContain('banana');
  });

  test('Jest puede manejar objetos', () => {
    const user = { name: 'John', age: 30 };
    expect(user).toHaveProperty('name');
    expect(user.name).toBe('John');
  });
});
