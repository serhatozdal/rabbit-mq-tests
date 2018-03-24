CREATE STREAM test.user_visit_test(
  visit_id TEXT NOT NULL,
  site_id TEXT NOT NULL,
  action_name VARCHAR(250),
  url TEXT NOT NULL,
  random_value TEXT,
  referrer_url TEXT,
  resolution VARCHAR(100) NOT NULL,
  hour INTEGER NOT NULL DEFAULT 0,
  minute INTEGER NOT NULL DEFAULT 0,
  second INTEGER NOT NULL DEFAULT 0,
  user_agent TEXT NOT NULL,
  language VARCHAR(10) NOT NULL,

  flash INTEGER,
  java INTEGER,
  quick_time INTEGER,
  real_player INTEGER,
  pdf INTEGER,
  windows_media INTEGER,
  silverlight INTEGER,

  ip_address INET NOT NULL,
  created_date TIMESTAMP DEFAULT NOW()
);