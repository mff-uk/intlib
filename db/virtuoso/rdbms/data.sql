fk_check_input_values(0);
-- dbdump: dumping datasource "localhost:1111", username=dba
-- tablequalifier=NULL  tableowner=NULL  tablename=is given, one or more  tabletype=NULL
-- Connected to datasource "OpenLink Virtuoso", Driver v. 06.01.3127 OpenLink Virtuoso ODBC Driver.
-- get_all_tables: tablepattern="db.intlib.%",11
-- Definitions of 13 tables were read in.
-- SELECT * FROM DB.INTLIB.DPU_ICONFIG_PAIRS
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_ICONFIG_PAIRS(conf_id,c_property,c_value) VALUES(1,'GraphsUri',?);
ACED0005737200146A6176612E7574696C2E4C696E6B65644C6973740C29535D
4A6088220300007870770400000001740012687474703A2F2F64627065646961
2E6F726778
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_ICONFIG_PAIRS(conf_id,c_property,c_value) VALUES(1,'SPARQL_endpoint',?);
ACED0005740019687474703A2F2F646270656469612E6F72672F73706172716C
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_ICONFIG_PAIRS(conf_id,c_property,c_value) VALUES(1,'SPARQL_query',?);
ACED0005740074434F4E535452554354207B3C687474703A2F2F646270656469
612E6F72672F7265736F757263652F5072616775653E203F70203F6F7D207768
657265207B3C687474703A2F2F646270656469612E6F72672F7265736F757263
652F5072616775653E203F70203F6F207D204C494D495420313030
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_ICONFIG_PAIRS(conf_id,c_property,c_value) VALUES(2,'DirectoryPath',?);
ACED000574000B2F746D702F696E746C6962
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_ICONFIG_PAIRS(conf_id,c_property,c_value) VALUES(2,'FileName',?);
ACED000574000B646270656469612E726466
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_ICONFIG_PAIRS(conf_id,c_property,c_value) VALUES(2,'RDFFileFormat',?);
ACED00057E720031637A2E63756E692E7872672E696E746C69622E636F6D6D6F
6E732E646174612E7264662E524446466F726D61745479706500000000000000
001200007872000E6A6176612E6C616E672E456E756D00000000000000001200
007870740006524446584D4C
END
-- Table DB.INTLIB.DPU_ICONFIG_PAIRS 6 rows output.
-- SELECT * FROM DB.INTLIB.DPU_INSTANCE
INSERT INTO DB.INTLIB.DPU_INSTANCE(id,name,description,dpu_id) VALUES(1,'RDF Extractor','Extracts RDF data.',1);
INSERT INTO DB.INTLIB.DPU_INSTANCE(id,name,description,dpu_id) VALUES(2,'File Loader','Loads RDF data into file.',5);
-- Table DB.INTLIB.DPU_INSTANCE 2 rows output.
-- SELECT * FROM DB.INTLIB.DPU_INSTANCE_CONFIG
INSERT INTO DB.INTLIB.DPU_INSTANCE_CONFIG(id,instance_id) VALUES(1,1);
INSERT INTO DB.INTLIB.DPU_INSTANCE_CONFIG(id,instance_id) VALUES(2,2);
-- Table DB.INTLIB.DPU_INSTANCE_CONFIG 2 rows output.
-- SELECT * FROM DB.INTLIB.DPU_MODEL
INSERT INTO DB.INTLIB.DPU_MODEL(id,name,description,type,visibility,jar_path) VALUES(1,'SPARQL Extractor','Extracts RDF data.',0,1,'RDF_extractor-0.0.1.jar');
INSERT INTO DB.INTLIB.DPU_MODEL(id,name,description,type,visibility,jar_path) VALUES(2,'RDF File Extractor','Extracts RDF data from a file.',0,1,'File_extractor-0.0.1.jar');
INSERT INTO DB.INTLIB.DPU_MODEL(id,name,description,type,visibility,jar_path) VALUES(3,'SPARQL Transformer','SPARQL Transformer.',1,1,'SPARQL_transformer-0.0.1.jar');
INSERT INTO DB.INTLIB.DPU_MODEL(id,name,description,type,visibility,jar_path) VALUES(4,'SPARQL Loader','Loads RDF data.',2,1,'RDF_loader-0.0.1.jar');
INSERT INTO DB.INTLIB.DPU_MODEL(id,name,description,type,visibility,jar_path) VALUES(5,'RDF File Loader','Loads RDF data into file.',2,1,'File_loader-0.0.1.jar');
-- Table DB.INTLIB.DPU_MODEL 5 rows output.
-- Table DB.INTLIB.DPU_RECORD has more than one blob column.
-- The column full_message of type LONG VARCHAR might not get properly inserted.
-- SELECT * FROM DB.INTLIB.DPU_RECORD
-- Table DB.INTLIB.DPU_RECORD has more than one blob column.
-- The column full_message of type LONG VARCHAR might not get properly inserted.
-- Table DB.INTLIB.DPU_RECORD 0 rows output.
-- SELECT * FROM DB.INTLIB.DPU_TCONFIG_PAIRS
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_TCONFIG_PAIRS(conf_id,c_property,c_value) VALUES(5,'DirectoryPath',?);
ACED000574000B2F746D702F696E746C6962
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_TCONFIG_PAIRS(conf_id,c_property,c_value) VALUES(5,'FileName',?);
ACED000574000B646270656469612E726466
END
FOREACH HEXADECIMAL BLOB INSERT INTO DB.INTLIB.DPU_TCONFIG_PAIRS(conf_id,c_property,c_value) VALUES(5,'RDFFileFormat',?);
ACED00057E720031637A2E63756E692E7872672E696E746C69622E636F6D6D6F
6E732E646174612E7264662E524446466F726D61745479706500000000000000
001200007872000E6A6176612E6C616E672E456E756D00000000000000001200
0078707400044155544F
END
-- Table DB.INTLIB.DPU_TCONFIG_PAIRS 3 rows output.
-- SELECT * FROM DB.INTLIB.DPU_TEMPLATE_CONFIG
INSERT INTO DB.INTLIB.DPU_TEMPLATE_CONFIG(id,dpu_id) VALUES(1,1);
INSERT INTO DB.INTLIB.DPU_TEMPLATE_CONFIG(id,dpu_id) VALUES(2,2);
INSERT INTO DB.INTLIB.DPU_TEMPLATE_CONFIG(id,dpu_id) VALUES(3,3);
INSERT INTO DB.INTLIB.DPU_TEMPLATE_CONFIG(id,dpu_id) VALUES(4,4);
INSERT INTO DB.INTLIB.DPU_TEMPLATE_CONFIG(id,dpu_id) VALUES(5,5);
-- Table DB.INTLIB.DPU_TEMPLATE_CONFIG 5 rows output.
-- SELECT * FROM DB.INTLIB.PPL_EDGE
INSERT INTO DB.INTLIB.PPL_EDGE(id,graph_id,node_from_id,node_to_id) VALUES(1,1,1,2);
-- Table DB.INTLIB.PPL_EDGE 1 rows output.
-- SELECT * FROM DB.INTLIB.PPL_EXECUTION
-- Table DB.INTLIB.PPL_EXECUTION 0 rows output.
-- SELECT * FROM DB.INTLIB.PPL_GRAPH
INSERT INTO DB.INTLIB.PPL_GRAPH(id,pipeline_id) VALUES(1,1);
-- Table DB.INTLIB.PPL_GRAPH 1 rows output.
-- SELECT * FROM DB.INTLIB.PPL_MODEL
INSERT INTO DB.INTLIB.PPL_MODEL(id,name,description) VALUES(1,'DBPedia','DBPedia SPARQL Extraction.');
-- Table DB.INTLIB.PPL_MODEL 1 rows output.
-- SELECT * FROM DB.INTLIB.PPL_NODE
INSERT INTO DB.INTLIB.PPL_NODE(id,graph_id,instance_id,position_id) VALUES(1,1,1,1);
INSERT INTO DB.INTLIB.PPL_NODE(id,graph_id,instance_id,position_id) VALUES(2,1,2,2);
-- Table DB.INTLIB.PPL_NODE 2 rows output.
-- SELECT * FROM DB.INTLIB.PPL_POSITION
INSERT INTO DB.INTLIB.PPL_POSITION(id,pos_x,pos_y) VALUES(1,141,126);
INSERT INTO DB.INTLIB.PPL_POSITION(id,pos_x,pos_y) VALUES(2,593,130);
-- Table DB.INTLIB.PPL_POSITION 2 rows output.


fk_check_input_values(1);
