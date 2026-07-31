// Test simple para verificar que la configuración de testing funciona
describe('Testing Setup', () => {
  test('basic math operations work', () => {
    expect(2 + 2).toBe(4);
    expect(5 * 3).toBe(15);
    expect(10 / 2).toBe(5);
  });

  test('string operations work', () => {
    expect('hello'.toUpperCase()).toBe('HELLO');
    expect('WORLD'.toLowerCase()).toBe('world');
    expect('test'.length).toBe(4);
  });

  test('array operations work', () => {
    const arr = [1, 2, 3];
    expect(arr.length).toBe(3);
    expect(arr.includes(2)).toBe(true);
    expect(arr.includes(5)).toBe(false);
  });

  test('object operations work', () => {
    const obj = { name: 'test', value: 42 };
    expect(obj.name).toBe('test');
    expect(obj.value).toBe(42);
    expect(Object.keys(obj)).toEqual(['name', 'value']);
  });
});
