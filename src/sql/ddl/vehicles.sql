SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
CREATE TABLE "public"."Vehicles" (gid serial,"name" varchar(254), "active" char(1));
ALTER TABLE "public"."Vehicles" ADD PRIMARY KEY (gid);
COMMIT;
