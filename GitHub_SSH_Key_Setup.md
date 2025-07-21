
# GitHub SSH Key Setup & Repository Creation – Step-by-Step Guide

## 🧩 Prerequisites

- Git must be installed on your system.  
- A GitHub account (https://github.com/)
- Terminal (Linux/macOS) or Git Bash (Windows)

---

## ✅ Step 1: Generate a New SSH Key

```bash
ssh-keygen -t ed25519 -C "your_email@example.com"
# Or if ed25519 is not supported
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

Accept default path or provide a custom one. Optionally add a passphrase.

---

## 📂 Step 2: Start SSH Agent and Add the Key

```bash
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
```

---

## 📋 Step 3: Copy the Public Key

```bash
cat ~/.ssh/id_ed25519.pub
```

Copy the contents manually or use:

- Linux: `xclip -sel clip < ~/.ssh/id_ed25519.pub`
- macOS: `pbcopy < ~/.ssh/id_ed25519.pub`
- Windows (Git Bash): `clip < ~/.ssh/id_ed25519.pub`

---

## 🌐 Step 4: Add SSH Key to Your GitHub Account

1. Go to [GitHub](https://github.com)
2. Profile → Settings → SSH and GPG Keys → New SSH Key
3. Add title and paste the key, then click **Add SSH Key**

---

## 🧪 Step 5: Test SSH Connection

```bash
ssh -T git@github.com
```

Respond with `yes` if prompted. You should see:

```
Hi <your-username>! You've successfully authenticated, but GitHub does not provide shell access.
```

---

## 📦 Step 6: Create a New GitHub Repository

1. Visit: https://github.com/new
2. Fill details → Click **Create repository**

---

## 🚀 Step 7: Clone the Repository Using SSH

```bash
git clone git@github.com:your-username/my-repo.git
```

---

## ✍️ Step 8: Start Working with Git

```bash
cd my-repo
touch README.md
git add README.md
git commit -m "Initial commit"
git push origin main
```

---

## 📌 Common Troubleshooting

| Error | Solution |
|------|----------|
| `Permission denied (publickey)` | Check if SSH key is added to GitHub and `ssh-agent` |
| `Could not resolve hostname` | Check your internet or proxy settings |
| `Repository not found` | Verify SSH URL and repo existence |

---

## ✅ Summary

| Step | Task |
|------|------|
| 1 | Generate SSH Key |
| 2 | Add key to SSH Agent |
| 3 | Copy public key |
| 4 | Add key to GitHub |
| 5 | Test SSH connection |
| 6 | Create GitHub repository |
| 7 | Clone via SSH |
| 8 | Start version control |
