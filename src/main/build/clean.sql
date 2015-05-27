DELETE FROM `dbmysql11`.`placescategories`
WHERE placeid <> '';
DELETE FROM `dbmysql11`.`places`
WHERE id <> '';
DELETE FROM `dbmysql11`.`categories`
WHERE id <> '';