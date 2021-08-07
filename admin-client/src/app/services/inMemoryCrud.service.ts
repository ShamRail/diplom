export interface InMemoryCrudService<T> {
  save(model: T): void;
  deleteById(id: number): boolean;
  updateById(id: number, model: T): boolean;
  findAll(): T[];
}
