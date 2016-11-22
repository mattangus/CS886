drop table if exists symmetric_aggs;
drop table if exists o_agents_profile;
drop table if exists strategies;
drop table if exists profiles;

create table profiles
(
	profile_id integer not null AUTO_INCREMENT,
	assignment varchar(255),
	num_strategies integer not null,
	PRIMARY KEY (profile_id)
);

create table strategies
(
	strategy_id integer NOT NULL AUTO_INCREMENT,
	evasiveSpeedChange double,
	evasiveAngleChange double,
	evasiveRelativeTimeThresh double,
	desiredSpeed double,
	accelerationTime double,
	privateSphere double,
	visualRange double,
	fieldOfView double,
	repulsiveStrengthCoefA double,
	repulsiveStrengthCoefB double,
	crosswalkPullStrengthCoefA double,
	crosswalkPullStrengthCoefB double,
	crosswalkPushStrengthCoefA double,
	crosswalkPushStrengthCoefB double,
	PRIMARY KEY (strategy_id)
);

create table o_agents_profile
(
	o_agents_profile_id integer NOT NULL,
	evasiveSpeedChange double NOT NULL,
	evasiveAngleChange double NOT NULL,
	evasiveRelativeTimeThresh double NOT NULL,
	desiredSpeed double NOT NULL,
	accelerationTime double NOT NULL,
	privateSphere double NOT NULL,
	visualRange double NOT NULL,
	fieldOfView double NOT NULL,
	repulsiveStrengthCoefA double NOT NULL,
	repulsiveStrengthCoefB double NOT NULL,
	crosswalkPullStrengthCoefA double NOT NULL,
	crosswalkPullStrengthCoefB double NOT NULL,
	crosswalkPushStrengthCoefA double NOT NULL,
	crosswalkPushStrengthCoefB double NOT NULL,
	PRIMARY KEY (o_agents_profile_id,evasiveSpeedChange,evasiveAngleChange,evasiveRelativeTimeThresh,desiredSpeed,accelerationTime,privateSphere,visualRange,fieldOfView,repulsiveStrengthCoefA,repulsiveStrengthCoefB,crosswalkPullStrengthCoefA,crosswalkPullStrengthCoefB,crosswalkPushStrengthCoefA,crosswalkPushStrengthCoefB)
);

create table symmetric_aggs
(
	profile_id integer,
	strategy_id integer,
	o_agents_profile_id integer,
	num_players integer,
	payoff double,
	PRIMARY KEY (profile_id, strategy_id),
	FOREIGN KEY (profile_id)
	REFERENCES profiles (profile_id),
	FOREIGN KEY (strategy_id)
	REFERENCES strategies (strategy_id),
	FOREIGN KEY (o_agents_profile_id)
	REFERENCES o_agents_profile (o_agents_profile_id)
);

CREATE OR REPLACE VIEW best_response_payoffs as
SELECT o_agents_profile_id, MAX(payoff) AS payoff
FROM symmetric_aggs
GROUP BY o_agents_profile_id;

/*
************test code ignore************

insert into profiles (profile_id, num_strategies) VALUES (1,1);
insert into profiles (profile_id, num_strategies) VALUES (2,2);
insert into profiles (profile_id, num_strategies) VALUES (3,1);

insert into strategies (strategy_id, strategy_name) VALUES (1,'Cooperate');
insert into strategies (strategy_id, strategy_name) VALUES (2,'Defect');

insert into o_agents_profile (o_agents_profile_id, o_agents_profile) VALUES (1,'Cooperate');
insert into o_agents_profile (o_agents_profile_id, o_agents_profile) VALUES (2,'Defect');

insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (1,1,1,2,8);
insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (2,2,1,1,10);
insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (2,1,2,1,0);
insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff) VALUES (3,2,2,2,5);

SELECT sa.profile_id
FROM symmetric_aggs sa, profiles p
WHERE sa.profile_id = p.profile_id
AND strategy_id in (1,2)
GROUP BY sa.profile_id, num_strategies
HAVING COUNT(*) = num_strategies;

SELECT DISTINCT profile_id
FROM 
(
SELECT o_agents_profile_id
FROM symmetric_aggs
WHERE profile_id = 3
) oa, symmetric_aggs sa
where oa.o_agents_profile_id = sa.o_agents_profile_id;

SELECT MAX(sa.payoff-oa.payoff)
FROM
(
SELECT o_agents_profile_id, payoff
FROM symmetric_aggs
WHERE profile_id = 3
) oa, symmetric_aggs sa
where oa.o_agents_profile_id = sa.o_agents_profile_id;

SELECT DISTINCT profile_id
FROM (
SELECT o_agents_profile_id
FROM subgame(:strats_in_support)
JOIN symmetric_aggs
USING(profile_id)
) oa, symmetric_aggs sa
where oa.o_agents_profile_id = sa.o_agents_profile_id;

SELECT sa.profile_id
FROM best_response_payoffs brp, symmetric_aggs sa, profiles p
where brp.o_agents_profile_id = sa.o_agents_profile_id
and brp.payoff = sa.payoff
and p.profile_id = sa.profile_id
and strategy_id in (select distinct profile_id from profiles)
GROUP BY profile_id, num_strategies
HAVING COUNT(*) = num_strategies;*/