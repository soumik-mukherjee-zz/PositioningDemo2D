SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
CREATE TABLE "public"."UrbanArea" (gid serial,"name" varchar(254), "city" varchar(254));
ALTER TABLE "public"."UrbanArea" ADD PRIMARY KEY (gid);
SELECT AddGeometryColumn('public','UrbanArea','geom','4326','POLYGON',2);
COMMIT;