- Select specific google place types we want to work with (e.g., no lawyers, 
	accountants etc.)
  - reflect the changes in fill_categories.sql

- Add some pre-initated values to our properties (edit fill_properties.sql)

- Fix schema and build_schema.sql:
  - change all "DbMysql11" to "trippy2"
  - remove all yago-related stuff (for example, yagoID column in some tables))

