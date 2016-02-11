SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
﻿CREATE TABLE "public"."Cities" (gid serial,
"name" varchar(254));
ALTER TABLE "public"."Cities" ADD PRIMARY KEY (gid);
SELECT AddGeometryColumn('public','Cities','geom','4326','POINT',2);
COMMIT;
