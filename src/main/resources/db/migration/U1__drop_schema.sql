DROP TRIGGER IF EXISTS update_task_modtime ON tasks;
DROP FUNCTION IF EXISTS update_modified_column();

DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;