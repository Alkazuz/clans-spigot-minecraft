package br.alkazuz.clans.objects;

public enum ClanRoles {
    LEADER("Líder", "§e●", true, true, true, true, true, true, true, true, true, true,  1),
    SUBLEADER("Sub Líder", "§7◍", true, true, true, true, true, true, true, true, true, true, 2),
    CAPTAIN("Capitão", "§7■", true, true, false, true, false, true, true, true, false, true,  3),
    MEMBER("Membro", "§7◻", false, false, false, false, false, false, true, true, false, true,  4),
    RECRUIT("Recruta", "§7-", false, false, false, false, false, false, false, false, false, false, 5);


    private String name;
    private String prefix;
    private boolean canInvite;
    private boolean canKick;
    private boolean canPromote;
    private boolean canDemote;
    private boolean canSetHome;
    private boolean canWithdraw;
    private boolean canDeposit;
    private boolean canHome;
    private boolean canToggleFriendlyFire;
    private boolean canGladiator;
    private int priority;

    private ClanRoles(final String name, String prefix, final boolean canInvite, final boolean canKick, final boolean canPromote, final boolean canDemote, final boolean canSetHome, final boolean canWithdraw, final boolean canDeposit, final boolean canHome, final boolean canToggleFriendlyFire, final boolean canGladiator, final int priority) {
        this.name = name;
        this.prefix = prefix;
        this.canInvite = canInvite;
        this.canKick = canKick;
        this.canPromote = canPromote;
        this.canDemote = canDemote;
        this.canSetHome = canSetHome;
        this.canWithdraw = canWithdraw;
        this.canDeposit = canDeposit;
        this.canHome = canHome;
        this.canToggleFriendlyFire = canToggleFriendlyFire;
        this.canGladiator = canGladiator;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean canInvite() {
        return this.canInvite;
    }

    public boolean canKick() {
        return this.canKick;
    }

    public boolean canPromote() {
        return this.canPromote;
    }

    public boolean canDemote() {
        return this.canDemote;
    }

    public boolean canSetHome() {
        return this.canSetHome;
    }

    public boolean canWithdraw() {
        return this.canWithdraw;
    }

    public boolean canDeposit() {
        return this.canDeposit;
    }

    public boolean canHome() {
        return this.canHome;
    }

    public boolean canToggleFriendlyFire() {
        return this.canToggleFriendlyFire;
    }

    public boolean canGladiator() {
        return this.canGladiator;
    }

    public int getPriority() {
        return this.priority;
    }

    public static ClanRoles getRole(final String name) {
        for (final ClanRoles role : values()) {
            if (role.getName().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }
}
