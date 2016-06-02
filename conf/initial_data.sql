INSERT INTO oauth.oauth_ws (enabled, name, domain, scope_request_url) VALUES (true, 'ws1', 'http://localhost:9000', '/oauth/ws/scopes');
INSERT INTO oauth.oauth_clients (name, accessor_id, creation_time, public_key, passwd) VALUES ('client1', 'e7e97be6-36a5-45f0-b8a0-bbced6976df4', now(), 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk-gsHqqSiARZ475qblQU-ZsJj5Nlvg1zm5MxAuHW4LssgUlIW5nI9n8Q_qOFvDo80l7NM6eZGYZnfYZOScZqHOc6VIdTVM3IyqsQsqqqeEDhCwMoVq4INyQDMetXgflOLxFNJutcIubM04EUtveZ1OyEG-0HODe1we-bHMNv5lQznxuOtLkBMRao32xLGfZ2H4sSw7HsH1oNgUijX0hOINFNkxoI1FnC4NSaXtl4C8-ia3Xi12mnv_IV_xFV0bJTvHqq3V64_34xvmvJvPhQMmXwuvgNhuIGtcNOky1SCMDD7-Q80KSTde-4mCtOY4EG1NC2CmkdJ6y_kP-9V-ZcAwIDAQAB', '8F8XKMF7CLIUVST3WFSC');

INSERT INTO oauth.oauth_scopes (name, description, add_time, mod_time, ws_id) VALUES ('test1', 'test level', null, null, 1);
INSERT INTO oauth.oauth_url_pattern (pattern, method, returns, arguments, scope_id) VALUES ('/oauth/ws/data1', 'GET', 'json', null, 1);
INSERT INTO oauth.oauth_url_pattern (pattern, method, returns, arguments, scope_id) VALUES ('/test', 'GET', 'text', null, 1);

INSERT INTO oauth.oauth_client_scope (client_id, scope_id) VALUES (1, 1)