INSERT INTO oauth.oauth_ws (enabled, name, domain, scope_request_url) VALUES (true, 'ws1', 'http://localhost:9000', '/oauth/ws/scopes');
INSERT INTO oauth.oauth_clients (name, accessor_id, creation_time, public_key, passwd) VALUES ('client1', '5147648c-0464-4182-a77f-c652b4d15a06', now(), 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjhAIYuDQkcle-9crJjjQeyTOvD44RDyru0c8EPX2611GjhuZJpl_qq9Y4Neh8PGbG3v_PHtPDilU-RCulo6SRMF4NlOEFi2qijG_eJVE9_Krf5Ob6hu8VlQh9VxLBV-Zvme46M5PSWzaUgqoYiAzTBGnjIeQo19FSMD6B2LlfxPbydatuingZdJtqvl2w99QTDKTOLEqj3Hy9WhUmbKYMs-GpYZDNRkRQS_yIwnV_bqO_uRHSeuO1xN8qsS67XHbiinoFF9QEnAY4rECvpqjy9FFLoY9rg64OklUGmApc2p_kruxUuBg8cF9mvRkDatLBUFNO4_fAiIGHsOqrpym6QIDAQAB', 'BQGRCTISTHQHWWNKNSIM');

INSERT INTO oauth.oauth_scopes (scope_id, name, description, add_time, mod_time, ws_id) VALUES (1, 'test1', 'test level', null, null, 1);
INSERT INTO oauth.oauth_url_pattern (url_pattern_id, pattern, method, returns, arguments, scope_id) VALUES (1, '/oauth/ws/data1', 'GET', 'json', null, 1);
INSERT INTO oauth.oauth_url_pattern (url_pattern_id, pattern, method, returns, arguments, scope_id) VALUES (2, '/test', 'GET', 'text', null, 1);

INSERT INTO oauth.oauth_client_scope (client_id, scope_id) VALUES (1, 1)