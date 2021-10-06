cd admin 
mvn install -Dmaven.test.skip=true 
cd ..

cd builder 
mvn install -Dmaven.test.skip=true
cd ..

cd runner
mvn install -Dmaven.test.skip=true
cd ..

cd admin-client
ng build --prod --base-href /admin/
cd ..

cd runner-client 
ng build --prod --base-href /run/
cd ..

cd site
mvn install -Dmaven.test.skip=true
cd ..
