import { useEffect, useState } from "react";
import { getIncidents } from "../services/incidentApi";

function StatCard({ title, value, description }) {
  return (
    <div className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
      <p className="text-sm text-slate-400">{title}</p>

      <h2 className="mt-2 text-3xl font-bold text-white">
        {value}
      </h2>

      <p className="mt-1 text-xs text-slate-500">
        {description}
      </p>
    </div>
  );
}

function SeverityBadge({ severity }) {
  const styles = {
    HIGH: "bg-red-500/10 text-red-400 border-red-500/20",
    MEDIUM: "bg-yellow-500/10 text-yellow-400 border-yellow-500/20",
    LOW: "bg-blue-500/10 text-blue-400 border-blue-500/20",
  };

  return (
    <span
      className={`rounded-full border px-3 py-1 text-xs font-medium ${
        styles[severity] ||
        "bg-slate-500/10 text-slate-400 border-slate-500/20"
      }`}
    >
      {severity || "UNKNOWN"}
    </span>
  );
}

function StatusBadge({ status }) {
  const styles = {
    OPEN: "bg-orange-500/10 text-orange-400 border-orange-500/20",
    RESOLVED:
      "bg-green-500/10 text-green-400 border-green-500/20",
  };

  return (
    <span
      className={`rounded-full border px-3 py-1 text-xs font-medium ${
        styles[status] ||
        "bg-slate-500/10 text-slate-400 border-slate-500/20"
      }`}
    >
      {status || "UNKNOWN"}
    </span>
  );
}

function Dashboard() {
  const [incidents, setIncidents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchIncidents() {
      try {
        setLoading(true);
        setError(null);

        const data = await getIncidents();

        // Safety check
        setIncidents(Array.isArray(data) ? data : []);
      } catch (error) {
        console.error("Failed to load incidents:", error);

        setError("Unable to load incidents");
        setIncidents([]);
      } finally {
        setLoading(false);
      }
    }

    fetchIncidents();
  }, []);

  const activeIncidents = incidents.filter(
    (incident) => incident.status === "OPEN"
  ).length;

  const criticalIncidents = incidents.filter(
    (incident) => incident.severity === "HIGH"
  ).length;

  const resolvedIncidents = incidents.filter(
    (incident) => incident.status === "RESOLVED"
  ).length;

  return (
    <div className="min-h-screen bg-slate-950 text-white">
      {/* Header */}
      <header className="border-b border-slate-800">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-5">
          <div>
            <h1 className="text-2xl font-bold">
              RCA Engine
            </h1>

            <p className="text-sm text-slate-400">
              AI Powered Root Cause Analysis Platform
            </p>
          </div>

          <div className="flex items-center gap-2">
            <div className="h-2 w-2 rounded-full bg-green-400" />

            <span className="text-sm text-slate-400">
              System Healthy
            </span>
          </div>
        </div>
      </header>

      {/* Main */}
      <main className="mx-auto max-w-7xl px-6 py-8">
        {/* Page title */}
        <div className="mb-8">
          <p className="text-sm text-slate-500">
            Overview
          </p>

          <h2 className="mt-1 text-3xl font-semibold">
            Incident Intelligence
          </h2>

          <p className="mt-2 text-slate-400">
            Monitor production incidents and AI-generated
            root cause analysis.
          </p>
        </div>

        {/* Stats */}
        <div className="grid gap-5 md:grid-cols-2 lg:grid-cols-4">
          <StatCard
            title="Total Incidents"
            value={incidents.length}
            description="Detected incidents"
          />

          <StatCard
            title="Active Incidents"
            value={activeIncidents}
            description="Currently open"
          />

          <StatCard
            title="Critical"
            value={criticalIncidents}
            description="High severity issues"
          />

          <StatCard
            title="Resolved"
            value={resolvedIncidents}
            description="Resolved incidents"
          />
        </div>

        {/* Recent incidents */}
        <section className="mt-10">
          <div className="mb-5">
            <h2 className="text-xl font-semibold">
              Recent Incidents
            </h2>

            <p className="mt-1 text-sm text-slate-500">
              Latest production issues detected by the RCA engine.
            </p>
          </div>

          {/* Loading */}
          {loading && (
            <div className="rounded-2xl border border-slate-800 bg-slate-900 p-8 text-center">
              <p className="text-slate-400">
                Loading incidents...
              </p>
            </div>
          )}

          {/* Error */}
          {!loading && error && (
            <div className="rounded-2xl border border-red-500/20 bg-red-500/10 p-6">
              <p className="font-medium text-red-400">
                {error}
              </p>

              <p className="mt-1 text-sm text-red-400/70">
                Make sure the RCA backend is running on port 8080.
              </p>
            </div>
          )}

          {/* Empty */}
          {!loading &&
            !error &&
            incidents.length === 0 && (
              <div className="rounded-2xl border border-slate-800 bg-slate-900 p-8 text-center">
                <p className="text-slate-400">
                  No incidents found.
                </p>
              </div>
            )}

          {/* Incidents */}
          {!loading &&
            !error &&
            incidents.length > 0 && (
              <div className="space-y-4">
                {incidents.map((incident) => (
                  <div
                    key={incident.id}
                    className="rounded-2xl border border-slate-800 bg-slate-900 p-6 transition hover:border-slate-700"
                  >
                    <div className="flex flex-col justify-between gap-5 lg:flex-row lg:items-center">
                      {/* Incident information */}
                      <div className="min-w-0">
                        <div className="flex flex-wrap items-center gap-3">
                          <h3 className="text-lg font-semibold">
                            {incident.serviceName}
                          </h3>

                          <SeverityBadge
                            severity={incident.severity}
                          />

                          <StatusBadge
                            status={incident.status}
                          />
                        </div>

                        <p className="mt-3 font-medium text-slate-300">
                          {incident.title}
                        </p>

                        <p className="mt-2 max-w-3xl text-sm leading-6 text-slate-500">
                          {incident.description}
                        </p>

                        <p className="mt-3 text-xs text-slate-600">
                          Incident ID: #{incident.id}
                        </p>
                      </div>

                      {/* Action */}
                      <button
                        type="button"
                        className="shrink-0 rounded-lg bg-white px-5 py-2.5 text-sm font-medium text-slate-950 transition hover:bg-slate-200"
                      >
                        View RCA
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
        </section>
      </main>
    </div>
  );
}

export default Dashboard;