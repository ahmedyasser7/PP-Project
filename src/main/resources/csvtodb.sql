create database csvtodb;
use csvtodb;
select * from csvtodbdata;
SELECT name FROM sys.databases;
-- EXEC sp_help 'csvtodbdata'-- ;
-- Alternatively, you can use:
-- -- SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH 
-- -- FROM INFORMATION_SCHEMA.COLUMNS 
-- -- WHERE TABLE_NAME = 'csvtodbdata';