CREATE DATABASE AviationModelling;
GO

USE [AviationModelling]
GO
/****** Object:  Table [dbo].[authority]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[authority](
	[id] [int] NULL,
	[name] [varchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[db_user]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[db_user](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](100) NULL,
	[email] [varchar](100) NULL,
	[password] [varchar](100) NULL,
	[vault_login] [varchar](100) NULL,
	[vault_password] [varchar](100) NULL,
	[vault_url] [varchar](100) NULL,
 CONSTRAINT [user_pk] PRIMARY KEY NONCLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[event]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[event](
	[event_id] [int] NOT NULL,
	[event_name] [varchar](100) NULL,
	[event_location] [varchar](100) NULL,
	[event_start_date] [varchar](100) NULL,
	[event_end_date] [varchar](100) NULL,
	[event_type] [varchar](100) NULL,
	[number_of_rounds] [int] NULL,
 CONSTRAINT [PK_event] PRIMARY KEY CLUSTERED 
(
	[event_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[event_pilot]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[event_pilot](
	[event_pilot_id] [int] IDENTITY(1,1) NOT NULL,
	[pilot_id] [int] NULL,
	[event_id] [int] NULL,
	[discarded1] [float] NULL,
	[discarded2] [float] NULL,
	[score] [float] NULL,
	[total_penalty] [int] NULL,
	[percentage] [float] NULL,
 CONSTRAINT [event_pilot_pk] PRIMARY KEY NONCLUSTERED 
(
	[event_pilot_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[event_round]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[event_round](
	[event_round_id] [int] IDENTITY(1,1) NOT NULL,
	[event_id] [int] NOT NULL,
	[round_num] [int] NOT NULL,
	[is_finished] [bit] NULL,
	[is_cancelled] [bit] NULL,
	[number_of_groups] [int] NULL,
	[is_synchronized] [bit] NULL,
 CONSTRAINT [event_round_pk] PRIMARY KEY NONCLUSTERED 
(
	[event_round_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[flight]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[flight](
	[event_round_id] [int] NOT NULL,
	[event_pilot_id] [int] NOT NULL,
	[penalty] [int] NULL,
	[order_num] [int] NULL,
	[group_num] [varchar](100) NULL,
	[flight_time] [datetime] NULL,
	[wind_avg] [float] NULL,
	[dir_avg] [float] NULL,
	[seconds] [float] NULL,
	[sub1] [float] NULL,
	[sub2] [float] NULL,
	[sub3] [float] NULL,
	[sub4] [float] NULL,
	[sub5] [float] NULL,
	[sub6] [float] NULL,
	[sub7] [float] NULL,
	[sub8] [float] NULL,
	[sub9] [float] NULL,
	[sub10] [float] NULL,
	[sub11] [float] NULL,
	[dns] [bit] NULL,
	[dnf] [bit] NULL,
	[score] [float] NULL,
	[is_synchronized] [bit] NULL,
 CONSTRAINT [PK_flight] PRIMARY KEY CLUSTERED 
(
	[event_round_id] ASC,
	[event_pilot_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[pilot]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[pilot](
	[pilot_id] [int] NOT NULL,
	[pilot_bib] [varchar](50) NULL,
	[first_name] [varchar](50) NULL,
	[last_name] [varchar](50) NULL,
	[pilot_class] [varchar](50) NULL,
	[ama] [varchar](50) NULL,
	[fai] [varchar](50) NULL,
	[fai_license] [varchar](50) NULL,
	[team_name] [varchar](50) NULL,
 CONSTRAINT [PK_pilot] PRIMARY KEY CLUSTERED 
(
	[pilot_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[round]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[round](
	[round_num] [int] NOT NULL,
 CONSTRAINT [PK_round] PRIMARY KEY CLUSTERED 
(
	[round_num] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users_authorities]    Script Date: 08.10.2020 16:16:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users_authorities](
	[user_id] [int] NOT NULL,
	[authority_id] [int] NOT NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[event_round] ADD  DEFAULT ((0)) FOR [is_finished]
GO
ALTER TABLE [dbo].[event_round] ADD  DEFAULT ((0)) FOR [is_cancelled]
GO
ALTER TABLE [dbo].[event_round] ADD  DEFAULT ((0)) FOR [is_synchronized]
GO
ALTER TABLE [dbo].[event_pilot]  WITH CHECK ADD  CONSTRAINT [event_pilot_event_event_id_fk] FOREIGN KEY([event_id])
REFERENCES [dbo].[event] ([event_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[event_pilot] CHECK CONSTRAINT [event_pilot_event_event_id_fk]
GO
ALTER TABLE [dbo].[event_pilot]  WITH CHECK ADD  CONSTRAINT [event_pilot_pilot_pilot_id_fk] FOREIGN KEY([pilot_id])
REFERENCES [dbo].[pilot] ([pilot_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[event_pilot] CHECK CONSTRAINT [event_pilot_pilot_pilot_id_fk]
GO
ALTER TABLE [dbo].[event_round]  WITH CHECK ADD  CONSTRAINT [event_round_event_event_id_fk] FOREIGN KEY([event_id])
REFERENCES [dbo].[event] ([event_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[event_round] CHECK CONSTRAINT [event_round_event_event_id_fk]
GO
ALTER TABLE [dbo].[event_round]  WITH CHECK ADD  CONSTRAINT [event_round_round_round_num_fk] FOREIGN KEY([round_num])
REFERENCES [dbo].[round] ([round_num])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[event_round] CHECK CONSTRAINT [event_round_round_round_num_fk]
GO
ALTER TABLE [dbo].[flight]  WITH CHECK ADD  CONSTRAINT [flight_event_pilot_id_fk] FOREIGN KEY([event_pilot_id])
REFERENCES [dbo].[event_pilot] ([event_pilot_id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[flight] CHECK CONSTRAINT [flight_event_pilot_id_fk]
GO
ALTER TABLE [dbo].[flight]  WITH CHECK ADD  CONSTRAINT [flight_event_round_id_fk] FOREIGN KEY([event_round_id])
REFERENCES [dbo].[event_round] ([event_round_id])
GO
ALTER TABLE [dbo].[flight] CHECK CONSTRAINT [flight_event_round_id_fk]
GO
