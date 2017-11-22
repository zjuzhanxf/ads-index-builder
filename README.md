# ads-index-builder
**This is a command line app that builds forward index and invert index using ads data file and budget data file.**
1. clone the project
```
git clone https://github.com/zjuzhanxf/ads-index-builder.git
```
2. open the project with Intellij
3. Use brew to install memcached and start on port 11211
```
brew install memcached
/usr/local/bin/memcached -d -p 11211
```
4. Install MySQL and MySQLWorkbench, connect to MySQL, create schema search_ads_homework. Create table ads, and table Campaign with below query commands.
```
CREATE SCHEMA `search_ads_demo`;

CREATE TABLE `search_ads_demo`.`ad` (
  `adId` INT(11) NOT NULL,
  `campaignId` INT(11) NULL DEFAULT NULL,
  `keyWords` VARCHAR(1024) NULL DEFAULT NULL,
  `bidPrice` DOUBLE NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `thumbnail` MEDIUMTEXT NULL DEFAULT NULL,
  `description` MEDIUMTEXT NULL DEFAULT NULL,
  `brand` VARCHAR(100) NULL DEFAULT NULL,
  `detail_url` MEDIUMTEXT NULL DEFAULT NULL,
  `category` VARCHAR(1024) NULL DEFAULT NULL,
  `title` VARCHAR(2048) NULL DEFAULT NULL,
  PRIMARY KEY (`adId`));
  
CREATE TABLE `search_ads_demo`.`campaign` (
  `campaignId` INT(11) NOT NULL,
  `budget` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`campaignId`));
```
3. In Intellij, go to Run -> Edit Configurations -> Program Arguments, enter the path to two input files, i.e. ads_data.txt and budget_data.txt, which are in the src/resouces folder
4. Run the app. The table ads and Campaign is populated with the data in ads_data.txt and budget_data.txt, respectively.
