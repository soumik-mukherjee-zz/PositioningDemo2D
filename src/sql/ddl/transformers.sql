SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
CREATE TABLE "public"."Transformers" (gid serial,"name" varchar(254), "suburb" varchar(254), "city" varchar(254));
ALTER TABLE "public"."Transformers" ADD PRIMARY KEY (gid);
SELECT AddGeometryColumn('public','Transformers','geom','4326','MULTIPOINT',2);
COMMIT;
