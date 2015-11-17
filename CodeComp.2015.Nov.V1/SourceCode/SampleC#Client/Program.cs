using Autofac;
using RestSharp;
using RestSharp.Authenticators;
using System;
using System.Configuration;
using System.Linq;
using RBS.CodeComp.Client.IO;
using RBS.CodeComp.Client.Strategy;

namespace RBS.CodeComp.Client
{
    class Program
    {
        private static IContainer Container { get; set; }

        static void Main(string[] args)
        {
            string teamName = "FlyingBirds", password = "mypassword";

            if (args.Count() == 2)
            {
                teamName = args[0].Trim();
                password = args[1].Trim();
            }

            Console.Title = teamName;
            Console.BackgroundColor = ConsoleColor.White;
            Console.Clear();

            var builder = new ContainerBuilder();

            //Authenticator
            builder.RegisterType<HttpBasicAuthenticator>().As<IAuthenticator>().WithParameters(new Autofac.Core.Parameter[] 
            {
                new NamedParameter("username", teamName),
                new NamedParameter("password", password)
            });
            //Rest client
            builder.RegisterType<RestClient>().As<IRestClient>()
                .UsingConstructor(typeof(string)).WithParameters(new Autofac.Core.Parameter[] 
            {
                new NamedParameter("baseUrl", ConfigurationManager.AppSettings["ServerBaseAddress"])
            });
            //IO
            builder.RegisterType<Writer>().As<IWriter>().WithParameters(new Autofac.Core.Parameter[] 
            {
                new NamedParameter("teamName", teamName)
            });
            //Player
            builder.RegisterType<Player>().As<IPlayer>().WithParameters(new Autofac.Core.Parameter[] 
            {
                new NamedParameter("myTeamName", teamName)
            });
            //Algo
            builder.RegisterType<CardStrategy>().As<ICardStrategy>();

            Container = builder.Build();

            using (var scope = Container.BeginLifetimeScope())
            {
                var player = scope.Resolve<IPlayer>();

                player.Play();
            }
        }
    }
}
