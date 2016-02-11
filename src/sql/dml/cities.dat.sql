SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
INSERT INTO "public"."Cities"(name, geom)
    VALUES ('Kolkata', ST_GeomFromText('POINT(88.0883943 22.67548)', 4326));

INSERT INTO "public"."Cities"(name, geom)
    VALUES ('Durgapur', ST_GeomFromText('POINT(87.3139044 23.5257367)'), 4326));

INSERT INTO "public"."Cities"(name, geom)
    VALUES ('Siliguri', ST_GeomFromText('POINT(88.419811 26.719874)', 4326));
COMMIT;
ANALYZE "public"."UrbanArea";
